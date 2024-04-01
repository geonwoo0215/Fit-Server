package com.fit.fit_be.domain.like.fixture;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.member.model.Member;

public class LikeFixture {

    public static Likes createLike(Board board, Member member) {
        return Likes.of(board, member);
    }

}
