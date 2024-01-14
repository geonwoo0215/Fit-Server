package com.fit.fit_be.domain.member.dto;

import com.fit.fit_be.domain.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSingUpRequest {

    private String loginId;

    private String password;

    private String email;

    private String nickname;

    @Builder
    public MemberSingUpRequest(String loginId, String password, String email, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Member toMember(String encodePassword) {
        return new Member(loginId, encodePassword, email, nickname);
    }
}
