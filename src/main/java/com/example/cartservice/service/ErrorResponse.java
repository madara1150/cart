package com.example.cartservice.service;

public class ErrorResponse {
    private String message;
    private String cause;

    public ErrorResponse(String message, String cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }
}
