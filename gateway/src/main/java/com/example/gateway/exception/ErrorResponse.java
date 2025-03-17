package com.example.gateway.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    int status;
    String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
