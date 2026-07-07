package com.lendmate.orderservice.mapper;

import com.lendmate.orderservice.dto.requestDto.OrderRequest;
import com.lendmate.orderservice.dto.responseDto.OrderItemResponse;
import com.lendmate.orderservice.dto.responseDto.OrderResponse;
import com.lendmate.orderservice.model.Order;
import com.lendmate.orderservice.model.OrderItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final OrderItemMapper itemMapper;

    public OrderMapper(OrderItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public Order toEntity(OrderRequest request) {
        if (request == null){
            return null;
        }
        Order order = Order.builder()
                .orderNumber(request.getUserId() + "-" + System.currentTimeMillis())
                .userId(request.getUserId())
                .description(request.getDescription())
                .status(request.getStatus())
                .totalPrice(request.getTotalPrice())
                .addressId(request.getAddressId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        if (request.getItems() != null) {
            List<OrderItem> items = request.getItems().stream()
                    .map(itemMapper::toEntity)
                    .peek(item -> item.setOrder(order))
                    .collect(Collectors.toList());
            order.setItems(items);
        }
        return order;
    }

    public OrderResponse toDto(Order order) {
        if (order == null) return null;
        List<OrderItemResponse> items = null;
        if (order.getItems() != null) {
            items = order.getItems().stream().map(itemMapper::toDto).collect(Collectors.toList());
        }
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .description(order.getDescription())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .addressId(order.getAddressId())
                .items(items)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public void updateEntity(Order order, OrderRequest request) {
        //if (request.getUserId() != null) order.setUserId(request.getUserId());
        //if (request.getDescription() != null) order.setDescription(request.getDescription());
        if (request.getStatus() != null) order.setStatus(request.getStatus());
        //if (request.getTotalPrice() != null) order.setTotalPrice(request.getTotalPrice());
       // if (request.getAddressId() != null) order.setAddressId(request.getAddressId());
        order.setUpdatedAt(LocalDateTime.now());
    }
}
