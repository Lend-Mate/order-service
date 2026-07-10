package com.lendmate.orderservice.service.impl;

import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import com.lendmate.orderservice.dto.responseDto.OrderItemResponse;
import com.lendmate.orderservice.mapper.OrderItemMapper;
import com.lendmate.orderservice.model.Order;
import com.lendmate.orderservice.model.OrderItem;
import com.lendmate.orderservice.repository.OrderItemRepository;
import com.lendmate.orderservice.repository.OrderRepository;
import com.lendmate.orderservice.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper mapper;

    @Override
    public OrderItemResponse createOrderItem(Long orderId, OrderItemRequest request) {
       // Order order = orderRepository.findById(orderId).orElseThrow();
        OrderItem item = mapper.toEntity(request);
       // item.setOrder(orderId);
        OrderItem saved = itemRepository.save(item);
        return mapper.toDto(saved);
    }

    @Override
    public List<OrderItemResponse> getItemsByOrder(Long orderId) {
        return itemRepository.findByOrderId(orderId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteOrderItem(Long id) {
        itemRepository.deleteById(id);
    }
}
