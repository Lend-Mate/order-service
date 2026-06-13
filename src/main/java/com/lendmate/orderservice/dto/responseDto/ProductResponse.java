package com.lendmate.orderservice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private Long ownerId;
    private Long categoryId;
    private String productName;
    private String description;
    private Currency currency;
    private BigDecimal price;
    private String brand;
    private Integer stockQuantity;
    private Integer minRentalDays;
    private Integer maxRentalDays;
    private BigDecimal depositAmount;
    private List<ProductImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}