package com.fit.fit_be.domain.member.service;

import com.fit.fit_be.domain.member.dto.MemberSingUpRequest;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long singUp(MemberSingUpRequest memberSingUpRequest) {
        String encodePassword = encoder.encode(memberSingUpRequest.getPassword());
        Member member = memberSingUpRequest.toMember(encodePassword);
        Member saveMember = memberRepository.save(member);
        return saveMember.getId();
    }

}
