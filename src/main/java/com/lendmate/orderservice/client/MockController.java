package com.lendmate.orderservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockController {

    @GetMapping("/payment/{orderId}")
    public ResponseEntity<String> mockPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/deliver/{orderId}")
    public ResponseEntity<String> mockDelivered(@PathVariable Long orderId) {
        return ResponseEntity.ok("SUCCESS");
    }
}
