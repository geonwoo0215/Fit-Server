package com.fit.fit_be.domain.member.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

    public PasswordMismatchException(Object... params) {
        super(STATUS, params, MESSAGE);
    }

}
