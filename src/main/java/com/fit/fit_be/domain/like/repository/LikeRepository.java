package com.fit.fit_be.domain.like.repository;

import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    int deleteByBoard_IdAndMember(@Param("boardId") Long boardId, @Param("member") Member member);
}
