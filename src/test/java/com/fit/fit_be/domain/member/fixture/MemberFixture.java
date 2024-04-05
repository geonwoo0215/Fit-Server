package com.fit.fit_be.domain.member.fixture;

import com.fit.fit_be.domain.member.dto.request.DeleteMemberRequest;
import com.fit.fit_be.domain.member.dto.request.MemberSignUpRequest;
import com.fit.fit_be.domain.member.dto.request.UpdateMemberRequest;
import com.fit.fit_be.domain.member.model.Member;

public class MemberFixture {

    public static final String NICKNAME = "its_woo";
    public static final String EMAIL = "email@fit.com";
    public static final String PASSWORD = "abc12345678@";
    public static final String PROFILE_IMAGE_URL = "profileImageUrl";

    public static Member createMember() {
        return Member.builder()
                .nickname(NICKNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .build();
    }

    public static MemberSignUpRequest createMemberSignUpRequest() {
        return MemberSignUpRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }

    public static UpdateMemberRequest createUpdateMemberRequest() {
        return new UpdateMemberRequest(EMAIL, PASSWORD);
    }

    public static DeleteMemberRequest createDeleteMemberRequest() {
        return new DeleteMemberRequest(PASSWORD);
    }

}
