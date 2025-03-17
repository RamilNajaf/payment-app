package com.example.msaccount.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    int status;
    String message;

    public ErrorResponse(int status,String message) {
        this.status = status;
        this.message = message;
    }
}
