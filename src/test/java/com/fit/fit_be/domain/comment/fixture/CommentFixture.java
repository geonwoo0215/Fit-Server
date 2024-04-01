package com.fit.fit_be.domain.comment.fixture;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.comment.dto.request.CommentSaveRequest;
import com.fit.fit_be.domain.comment.model.Comment;
import com.fit.fit_be.domain.member.model.Member;

public class CommentFixture {

    private static final String COMMENT = "옷 이뻐요";
    private static final Long GROUP_NO = 0L;
    private static final Long NO_PARENT_ID = 0L;

    public static Comment createComment(Member member, Board board) {
        return Comment.of(member, board, COMMENT, GROUP_NO);
    }

    public static CommentSaveRequest createCommentSaveRequest() {
        return new CommentSaveRequest(NO_PARENT_ID, COMMENT);
    }

}
