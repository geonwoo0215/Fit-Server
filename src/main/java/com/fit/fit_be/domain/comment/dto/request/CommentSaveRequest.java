package com.fit.fit_be.domain.comment.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequest {

    private Long commentId;

    private String comment;

    public CommentSaveRequest(Long commentId, String comment) {
        this.commentId = commentId;
        this.comment = comment;
    }
}
