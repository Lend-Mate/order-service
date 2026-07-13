package com.lendmate.orderservice.client.product;

import com.lendmate.orderservice.client.product.dto.requestDto.ProductAvailabilityRequest;
import com.lendmate.orderservice.dto.responseDto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductServiceClient {

    @GetMapping("/products/batch")
    List<ProductResponse> getProductsByIds(@RequestParam("ids") List<Long> ids);

    @PostMapping("/product-availability")
    void createProductAvailabilityRecord(@RequestBody ProductAvailabilityRequest request);

    @PostMapping("/products/internal/quantities")
    Map<Long, Integer> getProductQuantities(@RequestBody List<Long> ids);
}
