package com.fit.fit_be.domain.member.controller;

import com.fit.fit_be.domain.member.dto.request.EmailCodeCheckRequest;
import com.fit.fit_be.domain.member.dto.request.MemberSingUpRequest;
import com.fit.fit_be.domain.member.service.MemberService;
import com.fit.fit_be.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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
}
