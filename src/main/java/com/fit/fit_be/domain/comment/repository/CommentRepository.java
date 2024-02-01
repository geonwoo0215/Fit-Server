package com.fit.fit_be.domain.comment.repository;

import com.fit.fit_be.domain.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COALESCE(MAX(c.groupNo), 0) FROM Comment c WHERE c.board.id = :boardId")
    Long findMaxGroupNoByBoardId(@Param("boardId") Long boardId);

    Page<Comment> findAllById(@Param("boardId") Long boardId, Pageable pageable);
}
