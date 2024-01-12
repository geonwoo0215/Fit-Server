package com.fit.fit_be.domain.member.repository;

import com.fit.fit_be.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
