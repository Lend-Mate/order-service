package com.lendmate.orderservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import com.lendmate.orderservice.dto.responseDto.OrderItemResponse;
import com.lendmate.orderservice.service.OrderItemService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/order-items")
@AllArgsConstructor
public class OrderItemController {
    private final OrderItemService itemService;

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<OrderItemResponse> create(@PathVariable Long orderId, @Valid @RequestBody OrderItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createOrderItem(orderId, request));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(itemService.getItemsByOrder(orderId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
