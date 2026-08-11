package com.lendmate.orderservice.cron;

import com.lendmate.orderservice.dto.responseDto.OrderResponse;
import com.lendmate.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.time.LocalTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnotationScheduledJob {
    private final OrderService orderService;

    @Scheduled(cron = "#{@everySecondCron}")
    public void runEverySecondJob() {
        //log.info("[{}] Job SECONDLY executed - cron loaded from DB via @Scheduled", now());
        orderService.checkPendingPayments();
        orderService.convertConfirmedToDelivered();
    }

    @Scheduled(cron = "#{@everyMinuteCron}")
    public void runEveryMinuteJob() {
        //log.info("[{}] Job MINUTELY executed - cron loaded from DB via @Scheduled", now());
    }



}
