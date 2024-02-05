package com.fit.fit_be.global.exception;

import com.fit.fit_be.global.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(HttpServletRequest request, CustomException e) {

        log.warn("[EXCEPTION] REQUEST_URI [{}]", request.getRequestURI());
        log.warn("[EXCEPTION] HTTP_METHOD_TYPE [{}]", request.getMethod());
        log.warn("[EXCEPTION] EXCEPTION_TYPE [{}]", e.getClass().getSimpleName());
        log.warn("[EXCEPTION] EXCEPTION_PARAMS [{}]", e.getParams());
        log.warn("[EXCEPTION] EXCEPTION_MESSAGE [{}]", e.getMessage());
        log.warn("[EXCEPTION] FIELD_ERROR [{}]", e.getClass().getSimpleName());

        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {

        log.warn("[EXCEPTION] REQUEST_URI [{}]", request.getRequestURI());
        log.warn("[EXCEPTION] HTTP_METHOD_TYPE [{}]", request.getMethod());
        log.warn("[EXCEPTION] EXCEPTION_TYPE [{}]", e.getClass().getSimpleName());
        log.warn("[EXCEPTION] EXCEPTION_MESSAGE [{}]", Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        log.warn("[EXCEPTION] FIELD_ERROR [{}]", e.getBindingResult().getFieldError());

        return new ErrorResponse(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
