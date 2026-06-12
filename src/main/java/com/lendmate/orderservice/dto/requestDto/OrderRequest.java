package com.lendmate.orderservice.dto.requestDto;

import java.math.BigDecimal;
import java.util.List;

import com.lendmate.orderservice.model.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull
    private Long userId;

    private String description;

    private OrderStatus status;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private Long addressId;

    private List<OrderItemRequest> items;
}
