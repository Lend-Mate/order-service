package com.lendmate.orderservice.event.listener;

import com.lendmate.orderservice.kafka.producer.StockProducer;
import com.lendmate.orderservice.kafka.event.StockDecreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class StockEventListener {
    private final StockProducer stockProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(StockDecreaseEvent event) {
        stockProducer.sendStockEvent(event);
    }
}
