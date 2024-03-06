package com.fit.fit_be.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    private String profileImageUrl;

    private String email;

    private String nickname;

    @Builder
    public MemberResponse(String profileImageUrl, String email, String nickname) {
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.nickname = nickname;
    }
}
