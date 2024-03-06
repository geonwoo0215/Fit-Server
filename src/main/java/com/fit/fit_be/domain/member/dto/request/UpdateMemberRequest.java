package com.fit.fit_be.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberRequest {

    private String email;

    private String password;

    public UpdateMemberRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
