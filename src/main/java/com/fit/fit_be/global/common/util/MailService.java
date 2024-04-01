package com.fit.fit_be.global.common.util;

import com.fit.fit_be.domain.member.exception.EmailSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailService {

    private static final String MAIL_SUBJECT = "[FIT] 이메일 인증 코드";
    private static final Integer CODE_DURATION = 300;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    public void sendCodeEmail(String code, String email) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String mailText = String.format("인증 코드: %s\n앱으로 돌아가서 인증을 완료해주세요", code);

        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(MAIL_SUBJECT);
        simpleMailMessage.setText(mailText);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new EmailSendException(e);
        }

        redisUtil.setData(email, code, CODE_DURATION);
    }

}
