package com.lendmate.orderservice.repository;

import com.lendmate.orderservice.model.Cron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronConfigRepository extends JpaRepository<Cron, Long> {
}
