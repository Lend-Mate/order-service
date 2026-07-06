package com.lendmate.orderservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.lendmate.orderservice.client.MockClient;
import com.lendmate.orderservice.event.factory.OrderEventFactory;
import com.lendmate.orderservice.kafka.producer.OrderProducer;
import com.lendmate.orderservice.model.OrderStatus;
import com.lendmate.orderservice.service.OrderNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lendmate.orderservice.dto.requestDto.OrderRequest;
import com.lendmate.orderservice.dto.responseDto.OrderResponse;
import com.lendmate.orderservice.mapper.OrderItemMapper;
import com.lendmate.orderservice.mapper.OrderMapper;
import com.lendmate.orderservice.model.Order;
import com.lendmate.orderservice.model.OrderItem;
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
        //TODO: alınan ürünün miktarı kontrol edilecek aki durum için yetersiz hatası verilecek!!!
        Order saved = orderRepository.save(order);
        eventPublisher.publishEvent(orderEventFactory.createOrderConfirmedEvent(saved.getId(), saved.getOrderNumber(), request));
        eventPublisher.publishEvent(orderEventFactory.createStockDecreaseEvent(saved.getId(), request));
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
    public void checkPendingPayments() {
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
}
