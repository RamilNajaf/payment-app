package com.example.gateway.constants;

import java.util.List;

public class Constants {
    public static final String AUTHORIZATION = "Authorization";
    public static final String X_USER_ID = "X-USER-ID";
    public static final String USER_ID = "user_id";
    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );


}