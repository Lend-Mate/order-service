package com.lendmate.orderservice.config;

import com.lendmate.orderservice.exception.CronExpressionNotFound;
import com.lendmate.orderservice.model.Cron;
import com.lendmate.orderservice.repository.CronConfigRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile("!test")
@EnableScheduling
public class CronLoaderConfig {

    @Bean
    String everySecondCron(CronConfigRepository repository) {
        return repository.findById(1L)
                .map(Cron::getCronExpression)
                .orElseThrow(()-> new CronExpressionNotFound("Cron expression not found"));
    }
    @Bean
    String everyMinuteCron(CronConfigRepository repository) {
        return repository.findById(3L)
                .map(Cron::getCronExpression)
                .orElseThrow(()-> new CronExpressionNotFound("Cron expression not found"));
    }
}
