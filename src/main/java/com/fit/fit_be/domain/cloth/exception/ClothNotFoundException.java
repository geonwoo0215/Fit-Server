package com.fit.fit_be.domain.cloth.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ClothNotFoundException extends CustomException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "옷 조회를 실패하였습니다.";

    public ClothNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
