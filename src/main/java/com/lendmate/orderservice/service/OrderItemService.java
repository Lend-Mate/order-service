package com.lendmate.orderservice.service;

import java.util.List;

import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import com.lendmate.orderservice.dto.responseDto.OrderItemResponse;

public interface OrderItemService {
    OrderItemResponse createOrderItem(Long orderId, OrderItemRequest request);
    List<OrderItemResponse> getItemsByOrder(Long orderId);
    void deleteOrderItem(Long id);
}
