package com.fit.fit_be.domain.comment.fixture;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.comment.dto.request.CommentSaveRequest;
import com.fit.fit_be.domain.comment.model.Comment;
import com.fit.fit_be.domain.member.model.Member;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommentFixture {

    private static final String COMMENT = "옷 이뻐요";
    private static final Long GROUP_NO = 0L;
    private static final Long NO_PARENT_ID = 0L;

    public static Comment createComment(Member member, Board board) {
        return Comment.of(member, board, COMMENT, GROUP_NO);
    }

    public static Comment createComment(Member member, Board board, Long groupNo) {
        return Comment.of(member, board, COMMENT, groupNo);
    }

    public static List<Comment> createComments(Member member, Board board, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> CommentFixture.createComment(member, board, (long) i))
                .collect(Collectors.toList());
    }

    public static CommentSaveRequest createCommentSaveRequest() {
        return new CommentSaveRequest(NO_PARENT_ID, COMMENT);
    }

}
