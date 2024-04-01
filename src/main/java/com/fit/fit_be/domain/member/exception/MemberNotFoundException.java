package com.fit.fit_be.domain.member.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "사용자 조회를 실패하였습니다.";

    public MemberNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }

}
