package com.lendmate.orderservice.cron;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.time.LocalTime.now;

@Slf4j
@Component
public class AnnotationScheduledJob {

    @Scheduled(cron = "#{@everySecondCron}")
    public void runEverySecondJob() {
        log.info("[{}] Job SECONDLY executed - cron loaded from DB via @Scheduled", now());

    }

    @Scheduled(cron = "#{@everyMinuteCron}")
    public void runEveryMinuteJob() {
        log.info("[{}] Job MINUTELY executed - cron loaded from DB via @Scheduled", now());
    }



}
