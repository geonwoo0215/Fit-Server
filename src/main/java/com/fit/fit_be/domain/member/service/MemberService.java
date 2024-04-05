package com.fit.fit_be.domain.member.service;

import com.fit.fit_be.domain.member.dto.request.EmailCodeCheckRequest;
import com.fit.fit_be.domain.member.dto.request.MemberSignUpRequest;
import com.fit.fit_be.domain.member.dto.request.UpdateMemberRequest;
import com.fit.fit_be.domain.member.dto.request.UpdateProfileImageRequest;
import com.fit.fit_be.domain.member.exception.EmailDuplicateException;
import com.fit.fit_be.domain.member.exception.MemberNotFoundException;
import com.fit.fit_be.domain.member.exception.PasswordMismatchException;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.common.util.MailService;
import com.fit.fit_be.global.common.util.RandomCodeGenerator;
import com.fit.fit_be.global.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final RedisUtil redisUtil;
    private final MailService mailService;

    @Transactional
    public Long singUp(MemberSignUpRequest memberSingUpRequest) {
        String encodePassword = encoder.encode(memberSingUpRequest.getPassword());
        Member member = memberSingUpRequest.toMember(encodePassword);
        Member saveMember = memberRepository.save(member);
        return saveMember.getId();
    }

    @Transactional
    public void updatePassword(UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findByEmail(updateMemberRequest.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(updateMemberRequest.getEmail()));
        String encodePassword = encoder.encode(updateMemberRequest.getPassword());
        member.updateMemberPassword(encodePassword);
    }

    @Transactional
    public void updateProfileImage(Member member, UpdateProfileImageRequest updateProfileImageRequest) {
        member.updateMemberProfileImageUrl(updateProfileImageRequest.getProfileImage());
    }

    @Transactional
    public void delete(String password, Member member) {
        if (!encoder.matches(password, member.getPassword())) {
            throw new PasswordMismatchException();
        }
        memberRepository.delete(member);
    }

    public void sendCodeEmail(String email) {
        String code = RandomCodeGenerator.generateCode();
        mailService.sendCodeEmail(code, email);
    }

    public void checkCode(EmailCodeCheckRequest emailCodeCheckRequest) {

        if (!redisUtil.hasKey(emailCodeCheckRequest.getEmail())) {
            throw new RuntimeException();
        }

        String code = redisUtil.get(emailCodeCheckRequest.getEmail());

        if (!Objects.equals(code, emailCodeCheckRequest.getCode())) {
            throw new RuntimeException();
        }

    }

    private void validateDuplicateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new EmailDuplicateException(email);
        });
    }

    private void validateExistEmail(String email) {
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException());
    }

}
