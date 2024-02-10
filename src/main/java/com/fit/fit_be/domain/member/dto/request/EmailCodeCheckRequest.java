package com.fit.fit_be.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailCodeCheckRequest {

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "인증 코드를 입력해주세요")
    private String code;

    @Builder
    public EmailCodeCheckRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
