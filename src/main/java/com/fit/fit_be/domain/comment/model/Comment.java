package com.fit.fit_be.domain.comment.model;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private Long parentId;

    private String comment;

    private Long groupNo;

    private Long commentOrder;


}
