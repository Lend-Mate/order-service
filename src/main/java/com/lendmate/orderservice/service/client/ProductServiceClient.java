package com.lendmate.orderservice.service.client;

import com.lendmate.orderservice.dto.responseDto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductServiceClient {

    @GetMapping("/products/batch")
    List<ProductResponse> getProductsByIds(@RequestParam("ids") List<Long> ids);
}
