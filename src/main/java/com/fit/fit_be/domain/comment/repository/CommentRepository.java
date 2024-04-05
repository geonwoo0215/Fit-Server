package com.fit.fit_be.domain.comment.repository;

import com.fit.fit_be.domain.comment.dto.response.CommentResponse;
import com.fit.fit_be.domain.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COALESCE(MAX(c.groupNo), 0) FROM Comment c WHERE c.board.id = :boardId")
    Long findMaxGroupNoByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT new com.fit.fit_be.domain.comment.dto.response.CommentResponse(c.id, c.comment, c.member.nickname, p.member.nickname) " +
            "FROM Comment c " +
            "LEFT JOIN Comment p ON c.parentCommentId = p.id " +
            "LEFT JOIN Member m1 ON m1.id = c.member.id " +
            "LEFT JOIN Member m2 ON m2.id = p.member.id " +
            "WHERE c.board.id = :boardId")
    Page<CommentResponse> findAllByBoardId(@Param("boardId") Long boardId, Pageable pageable);

}
