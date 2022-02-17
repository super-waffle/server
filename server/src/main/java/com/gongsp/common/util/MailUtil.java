package com.gongsp.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Autowired
    private Environment env;

    // 이메일 전송
    public Boolean sendEmail(String userEmail, String mailTitle, String mailContent) {
        String sendTo = userEmail;

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                message.setTo(sendTo);
                message.setFrom(sendFrom);
                message.setSubject(mailTitle);
                message.setText(mailContent + "<img src=\"https://i6a301.p.ssafy.io:8080/images/logo_email.png\">", true);     // true: html 형식 사용
            }
        };
        try {
            mailSender.send(preparator);
        } catch (MailException e) {
            return false;
        }
        return true;
    }
}
