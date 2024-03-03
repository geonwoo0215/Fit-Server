package com.fit.fit_be.global.auth.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    private static final String MESSAGE = "리프레시 토큰이 만려되었습니다.";

    public RefreshTokenExpiredException(Object... params) {
        super(STATUS, params, MESSAGE);
    }

}
