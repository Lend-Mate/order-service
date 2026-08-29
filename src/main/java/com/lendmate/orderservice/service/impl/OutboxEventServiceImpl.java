package com.lendmate.orderservice.service.impl;

import com.lendmate.orderservice.model.OutboxEvent;
import com.lendmate.orderservice.repository.OutboxEventRepository;
import com.lendmate.orderservice.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxEventServiceImpl implements OutboxEventService {
    private final OutboxEventRepository repository;

    @Override
    public void save(OutboxEvent outboxEvent) {
        repository.save(outboxEvent);
    }
}