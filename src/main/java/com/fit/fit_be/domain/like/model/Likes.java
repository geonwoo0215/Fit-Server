package com.fit.fit_be.domain.like.model;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @CreatedDate
    private LocalDate createdDate;

    private Likes(Board board, Member member) {
        this.board = board;
        this.member = member;
    }

    public static Likes of(Board board, Member member) {
        return new Likes(board, member);
    }
}
