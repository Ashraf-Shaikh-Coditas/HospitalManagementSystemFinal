package com.springboot.hospitalmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private String username;
    private String fieldName;
    private long fieldValue;

    public UserNotFoundException(String username, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : '%s'", username, fieldName, fieldValue));
        this.username = username;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return username;
    }

    public String getFieldName() {
        return fieldName;
    }

    public long getFieldValue() {
        return fieldValue;
    }
}
