package com.lendmate.orderservice.exception;

import com.lendmate.orderservice.dto.responseDto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CronExpressionNotFound.class)
    public ResponseEntity<ErrorResponse> handleException(CronExpressionNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(ProductQuantityIsInSufficient.class)
    public ResponseEntity<ErrorResponse> handleException(ProductQuantityIsInSufficient ex) {
        return ResponseEntity
                .status(HttpStatus.INSUFFICIENT_STORAGE)
                .body(new ErrorResponse(HttpStatus.INSUFFICIENT_STORAGE.value(), ex.getMessage(), ex.getBody()));
    }
}
