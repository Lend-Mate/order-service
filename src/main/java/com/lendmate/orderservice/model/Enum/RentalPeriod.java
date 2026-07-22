package com.lendmate.orderservice.model.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public enum RentalPeriod {
    ONE_MONTH(1, BigDecimal.ZERO),
    THREE_MONTH(3, new BigDecimal("0.10")),
    SIX_MONTH(6, new BigDecimal("0.15")),
    NINE_MONTH(9, new BigDecimal("0.20")),
    TWELVE_MONTH(12, new BigDecimal("0.25"));

    private final int months;

    private final BigDecimal discountRate;
}
