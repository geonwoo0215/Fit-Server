package com.fit.fit_be.domain.member.model;

import com.fit.fit_be.domain.member.dto.response.MemberResponse;
import com.fit.fit_be.global.common.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String email;

    private String nickname;

    private String profileImageUrl;

    @Builder
    public Member(String password, String email, String nickname, String profileImageUrl) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public MemberResponse toMemberResponse() {
        return MemberResponse.builder()
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public void updateMemberPassword(String encodePassword) {
        this.password = encodePassword;
    }

    public void updateMemberProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void validatePasswordMatch(String encodePassword) {
        if (!Objects.equals(encodePassword, password)) {
            throw new RuntimeException();
        }
    }
}
