package com.example.userms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterationResponse {

    private Long userId;
    private String username;
}
