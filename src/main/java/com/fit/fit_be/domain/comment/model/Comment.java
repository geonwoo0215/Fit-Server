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

    private Long parentCommentId;

    private String comment;

    private Long groupNo;


    @Builder
    private Comment(Member member, Board board, Long parentCommentId, String comment, Long groupNo) {
        this.member = member;
        this.board = board;
        this.parentCommentId = parentCommentId;
        this.comment = comment;
        this.groupNo = groupNo;
    }

    public static Comment of(Member member, Board board, Long parentCommentId, String comment, Long groupNo) {
        return new Comment(member, board, parentCommentId, comment, groupNo);
    }

    public static Comment of(Member member, Board board, String comment, Long groupNo) {
        return Comment.of(member, board, 0L, comment, groupNo);
    }

    public CommentResponse toCommentResponse(String parentCommentNickname) {
        return CommentResponse.builder()
                .id(id)
                .comment(comment)
                .nickname(member.getNickname())
                .parentCommentMemberNickname(parentCommentNickname)
                .build();
    }
}
