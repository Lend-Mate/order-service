package com.lendmate.orderservice.event.factory;

import com.lendmate.orderservice.dto.requestDto.OrderRequest;
import com.lendmate.orderservice.kafka.event.OrderEvent;
import com.lendmate.orderservice.kafka.event.StockDecreaseEvent;
import com.lendmate.orderservice.kafka.event.StockDecreaseItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class OrderEventFactory {

    public OrderEvent createOrderConfirmedEvent(Long orderId, String orderNumber, OrderRequest request) {
        return new OrderEvent(
                orderId,
                "ORDER_CONFIRMED",
                request.getUserId(),
                orderNumber
        );
    }

    public StockDecreaseEvent createStockDecreaseEvent(Long orderId, OrderRequest orderRequest){
        log.info("StockDecreaseEvent oluşturuluyor - orderId={}", orderId);
        List<StockDecreaseItem> stockItems = orderRequest.getItems().stream()
                .map(item -> {
                    log.debug("StockDecreaseItem oluşturuluyor - productId={}, quantity={}",
                            item.getProductId(), item.getQuantity());
                    return StockDecreaseItem.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .build();
                })
                .toList();

        StockDecreaseEvent event = StockDecreaseEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .orderId(orderId)
                .items(stockItems)
                .build();

        log.info("StockDecreaseEvent oluşturuldu - eventId={}, orderId={}, itemCount={}",
                event.getEventId(), orderId, stockItems.size());
        return event;
    }
}
