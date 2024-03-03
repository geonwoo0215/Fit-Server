package com.fit.fit_be.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {
    private Long id;
    private String comment;
    private String nickname;
    private String parentCommentMemberNickname;

    @Builder
    public CommentResponse(Long id, String comment, String nickname, String parentCommentMemberNickname) {
        this.id = id;
        this.comment = comment;
        this.nickname = nickname;
        this.parentCommentMemberNickname = parentCommentMemberNickname;
    }
}
