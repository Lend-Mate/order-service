package com.lendmate.orderservice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String message;
    private Object body;

    public ErrorResponse(String message)
    {
        super();
        this.message = message;
    }
}