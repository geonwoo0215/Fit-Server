package com.fit.fit_be.domain.comment.service;

import com.fit.fit_be.domain.board.exception.BoardNotFoundException;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.comment.dto.request.CommentSaveRequest;
import com.fit.fit_be.domain.comment.dto.response.CommentResponse;
import com.fit.fit_be.domain.comment.exception.CommentFoundException;
import com.fit.fit_be.domain.comment.model.Comment;
import com.fit.fit_be.domain.comment.repository.CommentRepository;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long save(Member member, Long boardId, CommentSaveRequest commentSaveRequest) {

        Long maxGroupNo = commentRepository.findMaxGroupNoByBoardId(boardId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        if (commentSaveRequest.getCommentId() == 0) {
            Comment comment = Comment.builder()
                    .member(member)
                    .board(board)
                    .parentCommentNickname(null)
                    .comment(commentSaveRequest.getComment())
                    .groupNo(maxGroupNo + 1)
                    .build();

            return commentRepository.save(comment).getId();

        } else {
            Comment parentComment = commentRepository.findById(commentSaveRequest.getCommentId())
                    .orElseThrow(() -> new CommentFoundException(commentSaveRequest.getCommentId()));

            Comment comment = Comment.builder()
                    .member(member)
                    .board(board)
                    .parentCommentNickname(parentComment.getMember().getNickname())
                    .comment(commentSaveRequest.getComment())
                    .groupNo(parentComment.getGroupNo())
                    .build();

            return commentRepository.save(comment).getId();

        }


    }

    public Page<CommentResponse> findAllByBoardId(Long boardId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllById(boardId, pageable);
        List<CommentResponse> commentResponseList = comments.stream()
                .map(Comment::toCommentResponse)
                .toList();
        PageImpl<CommentResponse> commentResponsePage = new PageImpl<>(commentResponseList, pageable, comments.getTotalElements());
        return commentResponsePage;
    }


}
