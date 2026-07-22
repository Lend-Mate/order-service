package com.lendmate.orderservice.dto.responseDto;

import com.lendmate.orderservice.model.Enum.Currency;
import com.lendmate.orderservice.model.Enum.RentalPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private Map<RentalPeriod, BigDecimal> rentalPeriodPrices;
    private BigDecimal depositAmount;
    private List<ProductImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}