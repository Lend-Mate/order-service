package com.lendmate.orderservice.client.product.mapper;

import com.lendmate.orderservice.client.product.dto.requestDto.ProductAvailabilityRequest;
import com.lendmate.orderservice.client.product.model.Enum.Reason;
import com.lendmate.orderservice.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class ProductAvailabilityMapper {

    public ProductAvailabilityRequest toDto(OrderItem orderItem){
        if (orderItem == null) return null;
        return ProductAvailabilityRequest.builder()
                .productId(orderItem.getProductId())
                .startDate(orderItem.getStartDate())
                .endDate(orderItem.getEndDate())
                .quantity(orderItem.getQuantity())
                .reason(Reason.RENTED)
                .build();
    }
}
