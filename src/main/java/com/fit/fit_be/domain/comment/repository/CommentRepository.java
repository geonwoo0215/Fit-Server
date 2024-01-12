package com.fit.fit_be.domain.comment.repository;

import com.fit.fit_be.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
