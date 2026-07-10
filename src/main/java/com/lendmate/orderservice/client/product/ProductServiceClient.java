package com.lendmate.orderservice.client.product;

import com.lendmate.orderservice.client.product.dto.requestDto.ProductAvailabilityRequest;
import com.lendmate.orderservice.client.product.dto.responseDto.ProductAvailabilityResponse;
import com.lendmate.orderservice.dto.requestDto.OrderItemRequest;
import com.lendmate.orderservice.dto.responseDto.ProductResponse;
import com.lendmate.orderservice.model.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:8080")
public interface ProductServiceClient {

    @GetMapping("/products/batch")
    List<ProductResponse> getProductsByIds(@RequestParam("ids") List<Long> ids);

    @PostMapping("/product-availability")
    void createProductAvailabilityRecord(@RequestBody ProductAvailabilityRequest request);
}
