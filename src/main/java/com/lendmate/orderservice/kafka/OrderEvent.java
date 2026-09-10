package com.lendmate.orderservice.kafka;

import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private UUID eventId;
    private Long orderId;
    private String status;
    private Long userId;
    private String orderNumber;
    private List<OrderItemRequest> items;
}
