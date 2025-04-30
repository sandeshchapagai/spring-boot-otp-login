package com.example.login.signin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private final String message;
    private final T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
