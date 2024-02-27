package com.fit.fit_be.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    private String email;

    private String nickname;

    @Builder
    public MemberResponse(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
