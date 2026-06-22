package com.lendmate.orderservice.service.impl;

import com.lendmate.orderservice.service.OrderNumberGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SequenceBasedOrderNumberGenerator implements OrderNumberGenerator {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public String generate() {
        Long nextVal = ((Number) entityManager
                .createNativeQuery("SELECT nextval('order_seq')")
                .getSingleResult()).longValue();
        return String.valueOf(nextVal);
    }
}
