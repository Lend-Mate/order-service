package com.lendmate.orderservice.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.lendmate.orderservice.dto.requestDto.CartRequest;
import com.lendmate.orderservice.dto.responseDto.CartResponse;
import com.lendmate.orderservice.model.Cart;

@Component
public class CartMapper {
    public Cart toEntity(CartRequest request) {
        if (request == null) return null;
        return Cart.builder()
                .productId(request.getProductId())
                .userId(request.getUserId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public CartResponse toDto(Cart cart) {
        if (cart == null) return null;
        return CartResponse.builder()
                .id(cart.getId())
                .productId(cart.getProductId())
                .userId(cart.getUserId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
