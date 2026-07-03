package com.lendmate.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cron_config")
public class Cron {

    @Id
    private Long id;

    private String cronExpression;
}
