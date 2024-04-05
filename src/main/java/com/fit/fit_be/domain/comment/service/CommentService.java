package com.fit.fit_be.domain.comment.service;

import com.fit.fit_be.domain.board.exception.BoardNotFoundException;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.comment.dto.request.CommentSaveRequest;
import com.fit.fit_be.domain.comment.dto.response.CommentResponse;
import com.fit.fit_be.domain.comment.exception.CommentNotFoundException;
import com.fit.fit_be.domain.comment.model.Comment;
import com.fit.fit_be.domain.comment.repository.CommentRepository;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long save(Member member, Long boardId, CommentSaveRequest commentSaveRequest) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        Comment comment;

        if (commentSaveRequest.getParentCommentId() == 0) {
            Long maxGroupNo = commentRepository.findMaxGroupNoByBoardId(boardId);
            comment = Comment.of(member, board, commentSaveRequest.getComment(), ++maxGroupNo);
        } else {
            Comment parentComment = commentRepository.findById(commentSaveRequest.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException(commentSaveRequest.getParentCommentId()));
            comment = Comment.of(member, board, commentSaveRequest.getParentCommentId(), commentSaveRequest.getComment(), parentComment.getGroupNo());
        }

        return commentRepository.save(comment).getId();
    }

    public Page<CommentResponse> findAllByBoardId(Long boardId, Pageable pageable) {
        Page<CommentResponse> comments = commentRepository.findAllByBoardId(boardId, pageable);
        return comments;
    }


}
