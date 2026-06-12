package com.lendmate.orderservice.service;

import java.util.List;

import com.lendmate.orderservice.dto.requestDto.CartRequest;
import com.lendmate.orderservice.dto.responseDto.CartResponse;

public interface CartService {
    CartResponse createCart(CartRequest request);
    List<CartResponse> getCartsByUser(Long userId);
    void deleteCart(Long id);
}
