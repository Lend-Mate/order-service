package com.lendmate.orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendmate.orderservice.model.OutboxEvent;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final ObjectMapper objectMapper;
    public OutboxEvent publishOrderCreated(OrderEvent orderEvent, Long orderId) throws JsonProcessingException {
        Context contextToInject = Context.current();

        Map<String, String> carrier = new HashMap<>();
        W3CTraceContextPropagator.getInstance().inject(
                contextToInject, carrier, Map::put
        );

        String traceParent = carrier.get("traceparent");
        if (traceParent == null || traceParent.isBlank()) {
            log.warn("Failed to inject traceparent - no active span or broken context. orderId={}", orderId);
        }

       return OutboxEvent.builder()
                .eventId(orderEvent.getEventId())
                .aggregateId(orderId.toString())
                .aggregateType("order")
                .type("orderCreated")
                .payload(objectMapper.writeValueAsString(orderEvent))
                .trace_parent(traceParent)
                .timestamp(Instant.now())
                .build();

    }



}
