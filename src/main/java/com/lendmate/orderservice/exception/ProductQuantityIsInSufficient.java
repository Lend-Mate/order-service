package com.lendmate.orderservice.exception;

public class ProductQuantityIsInSufficient extends RuntimeException {
    public ProductQuantityIsInSufficient() {}
    public ProductQuantityIsInSufficient(String msg) {
        super(msg);
    }
}
