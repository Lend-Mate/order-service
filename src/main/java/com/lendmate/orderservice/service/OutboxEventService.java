package com.lendmate.orderservice.service;

import com.lendmate.orderservice.model.OutboxEvent;

public interface OutboxEventService {

    void save(OutboxEvent outboxEvent);
}
