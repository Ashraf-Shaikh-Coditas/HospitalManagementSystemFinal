package com.springboot.hospitalmanagement.exception;

import org.springframework.http.HttpStatus;

public class JWTException extends RuntimeException {
    private HttpStatus status;
    private String errorMessage;

    public JWTException(String message, HttpStatus status, String errorMessage) {
        super(message);
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public JWTException(HttpStatus badRequest, String invalid_jwt_signature) {
        super(invalid_jwt_signature);
        status = badRequest;
        errorMessage = invalid_jwt_signature;
    }
}
