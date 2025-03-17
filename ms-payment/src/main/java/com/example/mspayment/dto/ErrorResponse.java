package com.example.mspayment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    int status;
    String path;
    String timestamp;
    Map<String, String> validationErr;

    public ErrorResponse(int status) {
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
    }
}
