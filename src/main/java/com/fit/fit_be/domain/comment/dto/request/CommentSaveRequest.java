package com.fit.fit_be.domain.comment.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequest {

    private Long parentCommentId;

    private String comment;

    public CommentSaveRequest(Long parentCommentId, String comment) {
        this.parentCommentId = parentCommentId;
        this.comment = comment;
    }
}
