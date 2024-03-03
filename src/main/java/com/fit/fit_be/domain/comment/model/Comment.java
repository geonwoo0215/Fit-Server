package com.fit.fit_be.domain.comment.model;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.comment.dto.response.CommentResponse;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String parentCommentNickname;

    private String comment;

    private Long groupNo;


    @Builder
    public Comment(Member member, Board board, String parentCommentNickname, String comment, Long groupNo) {
        this.member = member;
        this.board = board;
        this.parentCommentNickname = parentCommentNickname;
        this.comment = comment;
        this.groupNo = groupNo;
    }

    public CommentResponse toCommentResponse() {
        return CommentResponse.builder()
                .id(id)
                .comment(comment)
                .nickname(member.getNickname())
                .parentCommentMemberNickname(parentCommentNickname)
                .build();
    }
}
