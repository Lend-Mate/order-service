package com.lendmate.orderservice.client.product.dto.responseDto;

import com.lendmate.orderservice.client.product.model.Enum.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAvailabilityResponse {

    private Long id;

    private Long productId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Reason reason;

    private LocalDateTime createdAt;
}
