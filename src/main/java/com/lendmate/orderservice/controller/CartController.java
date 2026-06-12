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

import com.lendmate.orderservice.dto.requestDto.CartRequest;
import com.lendmate.orderservice.dto.responseDto.CartResponse;
import com.lendmate.orderservice.service.CartService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> create(@Valid @RequestBody CartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(request));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CartResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
