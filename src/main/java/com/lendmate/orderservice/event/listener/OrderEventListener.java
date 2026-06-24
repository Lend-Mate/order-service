package com.lendmate.orderservice.event.listener;

import com.lendmate.orderservice.kafka.producer.OrderProducer;
import com.lendmate.orderservice.kafka.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final OrderProducer orderProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        orderProducer.sendOrderEvent(event);
    }
}
