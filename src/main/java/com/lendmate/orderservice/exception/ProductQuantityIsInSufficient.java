package com.lendmate.orderservice.exception;

import lombok.Getter;

@Getter
public class ProductQuantityIsInSufficient extends RuntimeException {
    private Object body;

    public ProductQuantityIsInSufficient() {}
    public ProductQuantityIsInSufficient(String msg) {
        super(msg);
    }
    public ProductQuantityIsInSufficient(String msg, Object body) {
        super(msg);
        this.body = body;
    }
}
