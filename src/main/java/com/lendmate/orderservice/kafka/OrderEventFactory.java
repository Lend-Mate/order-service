package com.lendmate.orderservice.kafka;

import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import com.lendmate.orderservice.dto.requestDto.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class OrderEventFactory {

    public OrderEvent createOrderConfirmedEvent(Long orderId, String orderNumber, OrderRequest request, List<OrderItemRequest> items) {
        return new OrderEvent(
                UUID.randomUUID(),
                orderId,
                "ORDER_CONFIRMED",
                request.getUserId(),
                orderNumber,
                items
        );
    }
}
