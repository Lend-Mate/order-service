package com.lendmate.orderservice.client.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MockClient {
    private final RestTemplate template = new RestTemplate();

    public String getPaymentStatus(Long orderId) {
        log.info("Payment is success");
        return template.getForObject(
                "http://localhost:8082/mock/payment/" + orderId,
                String.class
        );
    }

    public String getConfirmedStatus(Long orderId) {
        log.info("Confirmed is success");
        return template.getForObject(
                "http://localhost:8082/mock/deliver/" + orderId,
                String.class
        );
    }
}
