package com.lendmate.orderservice.kafka.producer;

import com.lendmate.orderservice.kafka.event.StockDecreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockProducer {
    private final KafkaTemplate<String, StockDecreaseEvent> kafkaTemplate;

    public void sendStockEvent(StockDecreaseEvent stockDecreaseEvent){
        kafkaTemplate.send("quantity-decrease-topic", stockDecreaseEvent);
    }
}
