package com.fit.fit_be.global.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    private final HttpStatus status;

    private final Object[] params;

    private final String message;

    public CustomException(HttpStatus status, Object[] params, String message) {
        this.status = status;
        this.params = params;
        this.message = message;
    }
}