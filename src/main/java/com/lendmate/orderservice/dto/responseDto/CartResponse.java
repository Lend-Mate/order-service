package com.lendmate.orderservice.dto.responseDto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private Long productId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
