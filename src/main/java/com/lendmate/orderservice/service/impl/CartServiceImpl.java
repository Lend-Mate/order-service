package com.lendmate.orderservice.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.lendmate.orderservice.dto.responseDto.ProductResponse;
import com.lendmate.orderservice.client.product.ProductServiceClient;
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
    private final ProductServiceClient productServiceClient;
    private final CartMapper mapper;

    @Override
    public CartResponse createCart(CartRequest request) {
        Cart cart = mapper.toEntity(request);
        Cart saved = cartRepository.save(cart);
        return mapper.toDto(saved);
    }

    @Override
    public List<CartResponse> getCartsByUser(Long userId) {
        List<CartResponse> carts = cartRepository.findByUserId(userId).stream().map(mapper::toDto).toList();

        List<Long> productIds = carts.stream().map(CartResponse::getProductId).toList();

        List<ProductResponse> products = productServiceClient.getProductsByIds(productIds);

        for (CartResponse cart : carts) {
            Long productId = cart.getProductId();
            Optional<ProductResponse> product = products.stream().filter(p -> Objects.equals(p.getId(), productId)).findFirst();
            product.ifPresent(cart::setProduct);
        }

        return carts;
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
}
