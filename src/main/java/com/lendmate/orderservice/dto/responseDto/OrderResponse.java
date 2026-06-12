package com.lendmate.orderservice.dto.responseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.lendmate.orderservice.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String description;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private Long addressId;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
