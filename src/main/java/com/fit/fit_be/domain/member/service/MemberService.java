package com.fit.fit_be.domain.member.service;

import com.fit.fit_be.domain.member.dto.request.EmailCodeCheckRequest;
import com.fit.fit_be.domain.member.dto.request.MemberSingUpRequest;
import com.fit.fit_be.domain.member.exception.EmailDuplicateException;
import com.fit.fit_be.domain.member.exception.EmailSendException;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder encoder;
    private final RedisUtil redisUtil;


    @Transactional
    public Long singUp(MemberSingUpRequest memberSingUpRequest) {
        String encodePassword = encoder.encode(memberSingUpRequest.getPassword());
        Member member = memberSingUpRequest.toMember(encodePassword);
        Member saveMember = memberRepository.save(member);
        return saveMember.getId();
    }

    public void sendEmail(String email) {
        validateDuplicateEmail(email);

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        int randomNumber = 10000 + random.nextInt(90000);
        String code = String.valueOf(randomNumber);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String mailSubject = "[FIT] 이메일 인증 코드";
        String mailText = String.format("인증 코드: %s\n앱으로 돌아가서 인증을 완료해주세요", code);

        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(mailSubject);
        simpleMailMessage.setText(mailText);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new EmailSendException(e);
        }

        redisUtil.setData(email, code, 300);

    }

    public void checkCode(EmailCodeCheckRequest emailCodeCheckRequest) {

        String requestEmail = emailCodeCheckRequest.getEmail();
        String requestCode = emailCodeCheckRequest.getCode();

        if (!redisUtil.hasKey(requestEmail)) {
            throw new RuntimeException();
        }

        String code = redisUtil.get(requestEmail);

        if (!Objects.equals(code, requestCode)) {
            throw new RuntimeException();
        }

    }

    private void validateDuplicateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new EmailDuplicateException(email);
        });
    }

}
