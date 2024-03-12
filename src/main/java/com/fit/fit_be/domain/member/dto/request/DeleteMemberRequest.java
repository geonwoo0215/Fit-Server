package com.fit.fit_be.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteMemberRequest {

    private String password;

    public DeleteMemberRequest(String password) {
        this.password = password;
    }
}
