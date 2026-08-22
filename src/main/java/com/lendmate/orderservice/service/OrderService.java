package com.lendmate.orderservice.service;

import java.util.List;

import com.lendmate.orderservice.dto.requestDto.OrderRequest;
import com.lendmate.orderservice.dto.responseDto.OrderResponse;
import com.lendmate.orderservice.model.OrderStatus;

public interface OrderService {
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    OrderResponse createOrder(OrderRequest request);
    OrderResponse updateOrder(Long id, OrderRequest request);
    void deleteOrder(Long id);
    void deleteOrdersByUser(Long userId);
    List<OrderResponse> getOrdersByUserId(Long userId);
    void convertPendingToConfirmed();
    void convertConfirmedToDelivered();
    List<OrderResponse> getDeliveredOrders(Long userId);
}
