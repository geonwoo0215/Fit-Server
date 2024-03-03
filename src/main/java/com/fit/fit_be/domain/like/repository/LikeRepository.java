package com.fit.fit_be.domain.like.repository;

import com.fit.fit_be.domain.like.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    int deleteByBoard_IdAndMember_Id(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    boolean existsByBoard_IdAndMember_Id(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

}
