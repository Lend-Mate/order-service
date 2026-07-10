package com.lendmate.orderservice.mapper;

import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import com.lendmate.orderservice.dto.responseDto.OrderItemResponse;
import com.lendmate.orderservice.dto.responseDto.ProductResponse;
import com.lendmate.orderservice.model.OrderItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderItemMapper {

    public OrderItem toEntity(OrderItemRequest request) {
        if (request == null) return null;
        return OrderItem.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public OrderItemResponse toDto(OrderItem item) {
        if (item == null) return null;
        return OrderItemResponse.builder()
                .id(item.getId())
                .product(new ProductResponse())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .startDate(item.getStartDate())
                .endDate(item.getEndDate())
                .build();
    }
}
