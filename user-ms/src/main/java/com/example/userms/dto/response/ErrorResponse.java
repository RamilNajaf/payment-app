package com.example.userms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    int status;
    String message;
    Map<String, String> validationErr;
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public ErrorResponse(int status) {
        this.status = status;
        this.message = message;
    }
}
