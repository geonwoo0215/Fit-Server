package com.fit.fit_be.domain.member.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EmailDuplicateException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "이메일로 가입한 계정이 이미 존재합니다.";

    public EmailDuplicateException(Object... params) {
        super(STATUS, params, MESSAGE);
    }

}