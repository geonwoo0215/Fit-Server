package com.fit.fit_be.domain.like.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class LikeNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "좋아요 조회를 실패하였습니다.";

    public LikeNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }

}
