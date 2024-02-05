package com.fit.fit_be.domain.comment.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends CustomException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "댓글 조회를 실패하였습니다.";

    public CommentNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
