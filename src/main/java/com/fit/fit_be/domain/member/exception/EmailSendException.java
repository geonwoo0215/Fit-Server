package com.fit.fit_be.domain.member.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EmailSendException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String MESSAGE = "이메일을 전송하면서 문제가 생겼습니다. 이메일을 다시 확인해주세요.";

    public EmailSendException(Object... params) {
        super(STATUS, params, MESSAGE);
    }

}
