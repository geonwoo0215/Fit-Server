package com.fit.fit_be.domain.member.fixture;

import com.fit.fit_be.domain.member.model.Member;

public class MemberFixture {

    public static final String NICKNAME = "its_woo";
    public static final String EMAIL = "email@fit.com";
    public static final String PASSWORD = "1234";
    public static final String PROFILE_IMAGE_URL = "profileImageUrl";

    public static Member createMember() {
        return Member.builder()
                .nickname(NICKNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .build();
    }
}
