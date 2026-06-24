package com.lendmate.orderservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDecreaseEvent {
    private String eventId;
    private Long orderId;
    private List<StockDecreaseItem> items;
}
