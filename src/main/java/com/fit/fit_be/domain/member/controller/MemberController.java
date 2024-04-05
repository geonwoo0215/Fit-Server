package com.fit.fit_be.domain.member.controller;

import com.fit.fit_be.domain.member.dto.request.*;
import com.fit.fit_be.domain.member.dto.response.MemberResponse;
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
                    @RequestBody @Valid MemberSignUpRequest memberSingUpRequest,
                    HttpServletRequest request
            ) {
        Long id = memberService.singUp(memberSingUpRequest);
        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));
    }

    @GetMapping(value = "/members/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<MemberResponse>> getProfile
            (
                    @AuthenticationPrincipal Member member
            ) {
        MemberResponse memberResponse = member.toMemberResponse();
        return ResponseEntity.ok(new ApiResponse<>(memberResponse));
    }

    @GetMapping(value = "/members/email")
    public ResponseEntity<Void> sendEmail
            (
                    @RequestParam("email") String email
            ) {
        memberService.sendCodeEmail(email);
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

    @PatchMapping(value = "/members/password")
    public ResponseEntity<Void> updatePassword
            (
                    @RequestBody UpdateMemberRequest updateMemberRequest
            ) {
        memberService.updatePassword(updateMemberRequest);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping(value = "/members/tokens")
    public ResponseEntity<Void> refreshToken(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        String accessToken = tokenService.refreshAccessToken(refreshToken);
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/members/profile-image")
    public ResponseEntity<Void> updateProfileImage
            (
                    @AuthenticationPrincipal Member member,
                    @RequestBody UpdateProfileImageRequest updateProfileImageRequest
            ) {
        memberService.updateProfileImage(member, updateProfileImageRequest);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping(value = "/members")
    public ResponseEntity<Void> delete
            (
                    @RequestBody DeleteMemberRequest deleteMemberRequest,
                    @AuthenticationPrincipal Member member
            ) {
        memberService.delete(deleteMemberRequest.getPassword(), member);
        return ResponseEntity
                .noContent()
                .build();
    }
}
