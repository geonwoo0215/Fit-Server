package com.fit.fit_be.domain.member.controller;

import com.fit.fit_be.domain.member.dto.request.EmailCodeCheckRequest;
import com.fit.fit_be.domain.member.dto.request.MemberSingUpRequest;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.service.MemberService;
import com.fit.fit_be.global.auth.token.TokenService;
import com.fit.fit_be.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping(value = "/members", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> singUp
            (
                    @RequestBody @Valid MemberSingUpRequest memberSingUpRequest,
                    HttpServletRequest request
            ) {
        Long id = memberService.singUp(memberSingUpRequest);
        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));
    }

    @GetMapping(value = "/members/email")
    public ResponseEntity<Void> sendEmail
            (
                    @RequestParam("email") String email
            ) {
        memberService.sendEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/members/email")
    public ResponseEntity<Void> checkEmailCode
            (
                    @RequestBody @Valid EmailCodeCheckRequest emailCodeCheckRequest
            ) {
        memberService.checkCode(emailCodeCheckRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/members/tokens")
    public ResponseEntity<Void> refreshToken(
            @CookieValue("refreshToken") String refreshToken,
            @AuthenticationPrincipal Member member,
            HttpServletResponse response
    ) {
        String accessToken = tokenService.refreshAccessToken(refreshToken, member.getId());
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
