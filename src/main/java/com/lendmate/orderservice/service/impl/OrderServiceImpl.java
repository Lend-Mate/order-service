package com.lendmate.orderservice.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import com.lendmate.orderservice.dto.responseDto.OrderItemResponse;
import com.lendmate.orderservice.dto.responseDto.ProductResponse;
import com.lendmate.orderservice.client.mock.MockClient;
import com.lendmate.orderservice.client.product.dto.requestDto.ProductAvailabilityRequest;
import com.lendmate.orderservice.client.product.mapper.ProductAvailabilityMapper;
import com.lendmate.orderservice.event.factory.OrderEventFactory;
import com.lendmate.orderservice.exception.ProductQuantityIsInSufficient;
import com.lendmate.orderservice.kafka.event.OrderEvent;
import com.lendmate.orderservice.kafka.producer.OrderProducer;
import com.lendmate.orderservice.model.OrderItem;
import com.lendmate.orderservice.model.OrderStatus;
import com.lendmate.orderservice.model.OutboxEvent;
import com.lendmate.orderservice.service.CartService;
import com.lendmate.orderservice.service.OrderNumberGenerator;
import com.lendmate.orderservice.client.product.ProductServiceClient;
import com.lendmate.orderservice.service.OutboxEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lendmate.orderservice.dto.requestDto.OrderRequest;
import com.lendmate.orderservice.dto.responseDto.OrderResponse;
import com.lendmate.orderservice.mapper.OrderItemMapper;
import com.lendmate.orderservice.mapper.OrderMapper;
import com.lendmate.orderservice.model.Order;
import com.lendmate.orderservice.repository.OrderRepository;
import com.lendmate.orderservice.service.OrderService;

import lombok.AllArgsConstructor;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderItemMapper itemMapper;
    private final OrderProducer orderProducer;
    private final OrderNumberGenerator orderNumberGenerator;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderEventFactory orderEventFactory;
    private final MockClient paymentClient;
    private final CartService cartService;
    private final ProductServiceClient productServiceClient;
    private final ProductAvailabilityMapper availabilityMapper;
    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return mapper.toDto(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = mapper.toEntity(request);
        order.setOrderNumber(orderNumberGenerator.generate());

       List<OrderItemRequest> itemRequests = notEnoughQuantityItems(request.getItems());
        if(!itemRequests.isEmpty()){
            throw new ProductQuantityIsInSufficient("Product Quantity is insufficient", itemRequests);
        }

        //TODO: NODE.JS PA kayıtlarındaki enddate şuanki tarihi geçerse cron ile ilgili productun quantity artırılacak!!!
        Order saved = orderRepository.save(order);
        List<OrderItem> orderItem = saved.getItems();
        orderItem.forEach((item) -> {
            ProductAvailabilityRequest availabilityRequest = availabilityMapper.toDto(item);
            productServiceClient.createProductAvailabilityRecord(availabilityRequest);
        });
        cartService.deleteCartByUserId(request.getUserId());



        //TODO: saga pattern: https://lend-mate.atlassian.net/jira/software/projects/KAN/boards/1?selectedIssue=KAN-61
        //eventPublisher.publishEvent(orderEventFactory.createOrderConfirmedEvent(saved.getId(), saved.getOrderNumber(), request, request.getItems()));
       // eventPublisher.publishEvent(orderEventFactory.createStockDecreaseEvent(saved.getId(), request));
        OrderEvent orderEvent = orderEventFactory.createOrderConfirmedEvent(saved.getId(), saved.getOrderNumber(), request, request.getItems());
        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateId(saved.getId().toString())
                    .aggregateType("order")
                    .type("order-confirmed-topic")
                    .payload(objectMapper.writeValueAsString(orderEvent))
                    .timestamp(Instant.now())
                    .build();

            outboxEventService.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id).orElseThrow();
        boolean isDeliveredAndCanceled = order.getStatus().equals(OrderStatus.DELIVERED) && request.getStatus().equals(OrderStatus.CANCELLED);
        if(isDeliveredAndCanceled){
        mapper.updateEntity(order, request);
        Order updated = orderRepository.save(order);
        return mapper.toDto(updated);
        }
        throw new RuntimeException();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findAll().stream().filter(o -> o.getUserId().equals(userId)).collect(Collectors.toList());
        orderRepository.deleteAll(orders);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void convertPendingToConfirmed() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);
        log.info("Orders size is {}", orders.size());
        orders.stream().filter(order -> !orders.isEmpty())
                .forEach(order -> {
                    String status = paymentClient.getPaymentStatus(order.getId());
                    if ("SUCCESS".equalsIgnoreCase(status)) {
                        order.setStatus(OrderStatus.CONFIRMED);
                        orderRepository.save(order);
                    }
                });

    }

    @Override
    public void convertConfirmedToDelivered() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.CONFIRMED);
        log.info("Orders size is {}", orders.size());
        orders.stream().filter(order -> !orders.isEmpty())
                .forEach(order -> {
                    String status = paymentClient.getConfirmedStatus(order.getId());
                    if ("SUCCESS".equalsIgnoreCase(status)) {
                        order.setStatus(OrderStatus.DELIVERED);
                        orderRepository.save(order);
                    }
                });

    }

    private boolean isOrderQuantityEnough(List<OrderItemRequest> items){
        List<Long> productIds = items.stream().map(OrderItemRequest::getProductId).toList();
        Map<Long, Integer> quantities = productServiceClient.getProductQuantities(productIds);

        return items.stream().allMatch(item -> {
            Integer availableStock = quantities.get(item.getProductId());
            return availableStock != null && item.getQuantity() <= availableStock;
        });
    }

    @Override
    public List<OrderResponse> getDeliveredOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdAndStatus(userId, OrderStatus.DELIVERED);
        log.info("Orders size is {}", orders.size());

        List<Long> productIds = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProductId)
                .distinct()
                .collect(Collectors.toList());

        List<ProductResponse> products = productServiceClient.getProductsByIds(productIds);

        List<OrderResponse> orderResponses = orders.stream().map(mapper::toDto).toList();
        for (Order order : orders) {
            for (int j = 0; j < order.getItems().size(); j++) {
                OrderItem item = order.getItems().get(j);
                OrderItemResponse itemResponse = itemMapper.toDto(item);
                products.stream()
                        .filter(p -> p.getId().equals(item.getProductId()))
                        .findFirst().ifPresent(itemResponse::setProduct);

                int finalJ = j;
                orderResponses.stream()
                        .filter(o -> o.getId().equals(order.getId()))
                        .findFirst()
                        .ifPresent(o -> o.getItems().set(finalJ, itemResponse));
            }
        }
        return orderResponses;
    }


    private List<OrderItemRequest> notEnoughQuantityItems(List<OrderItemRequest> items){
        List<Long> productIds = items.stream().map(OrderItemRequest::getProductId).toList();
        Map<Long, Integer> quantities = productServiceClient.getProductQuantities(productIds);

        return items.stream().map(item -> {
            Integer availableStock = quantities.get(item.getProductId());
            boolean isOK = availableStock != null && item.getQuantity() <= availableStock;
            if (!isOK) {
                item.setQuantity(availableStock);
                return item;
            }
            return null;
        }).filter(Objects::nonNull).toList();
    }
}
