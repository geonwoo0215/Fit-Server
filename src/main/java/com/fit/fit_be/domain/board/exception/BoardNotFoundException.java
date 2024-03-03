package com.fit.fit_be.domain.board.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "게시글 조회를 실패하였습니다.";

    public BoardNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }

}
