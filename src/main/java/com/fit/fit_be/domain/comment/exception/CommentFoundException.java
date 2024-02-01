package com.fit.fit_be.domain.comment.exception;

import com.fit.fit_be.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CommentFoundException extends CustomException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "댓글 조회를 실패하였습니다.";

    public CommentFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
