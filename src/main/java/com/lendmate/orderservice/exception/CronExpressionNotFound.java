package com.lendmate.orderservice.exception;

public class CronExpressionNotFound  extends RuntimeException{
    public CronExpressionNotFound (String message) {
        super(message);
    }
}
