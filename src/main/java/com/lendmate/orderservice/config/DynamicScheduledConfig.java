//package com.lendmate.orderservice.config;
//
//import com.lendmate.orderservice.exception.CronExpressionNotFound;
//import com.lendmate.orderservice.model.Cron;
//import com.lendmate.orderservice.repository.CronConfigRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//
//import static java.time.LocalTime.now;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class DynamicScheduledConfig implements SchedulingConfigurer {
//    private final CronConfigRepository repository;
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.addTriggerTask(() -> log.info("[{}] DynamicScheduledConfig executed - cron re-read from DB per execution", now()),
//                triggerContext -> {
//                    String cronExpression = repository.findById(1L)
//                            .map(Cron::getCronExpression)
//                            .orElseThrow(() -> new CronExpressionNotFound("Cron expression not found"));
//                    return new CronTrigger(cronExpression).nextExecution(triggerContext);
//                });
//
//    }
//}

//INFO: Dinamik bir şekilde db'den veri çeker runtime esnasında aktif çalışır!!! Şuan zaruri olmadığı için kullanılmayacaktır.!!!
