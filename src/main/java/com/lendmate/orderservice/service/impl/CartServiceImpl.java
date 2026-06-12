package com.lendmate.orderservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lendmate.orderservice.dto.requestDto.CartRequest;
import com.lendmate.orderservice.dto.responseDto.CartResponse;
import com.lendmate.orderservice.mapper.CartMapper;
import com.lendmate.orderservice.model.Cart;
import com.lendmate.orderservice.repository.CartRepository;
import com.lendmate.orderservice.service.CartService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper mapper;

    @Override
    public CartResponse createCart(CartRequest request) {
        Cart cart = mapper.toEntity(request);
        Cart saved = cartRepository.save(cart);
        return mapper.toDto(saved);
    }

    @Override
    public List<CartResponse> getCartsByUser(Long userId) {
        return cartRepository.findByUserId(userId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
}
