package com.lendmate.orderservice.client.product.dto.requestDto;

import com.lendmate.orderservice.client.product.model.Enum.Reason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAvailabilityRequest {

    @NotNull(message = "Product id is mandatory")
    private Long productId;

    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;

    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;

    @NotNull(message = "Reason is mandatory")
    private Reason reason;

    @NotNull(message = "Stock quantity is mandatory")
    private Integer quantity;
}
