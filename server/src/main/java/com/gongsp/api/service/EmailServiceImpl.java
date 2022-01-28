package com.gongsp.api.service;

import com.gongsp.common.util.UuidUtil;
import com.gongsp.common.util.MailUtil;
import com.gongsp.db.entity.AuthEmail;
import com.gongsp.db.repository.AuthRepository;
import com.gongsp.db.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UuidUtil uuidUtil;
    
    // 인증코드 발급
    @Override
    public String createAuthCode(String email) {
        String authCode = uuidUtil.createRandomUUID();
        AuthEmail existingAuthEmail = getByAuthEmail(email);
        // 인증코드 비교를 위해 저장
        // 기존 발급된 인증코드 있는 경우
        if (existingAuthEmail != null) {
            existingAuthEmail.setAuthCode(authCode);
            existingAuthEmail.setAuthDate(LocalDateTime.now());
            authRepository.save(existingAuthEmail);
        } else {
            // 인증코드 새로 발급
            AuthEmail authEmail = new AuthEmail();
            authEmail.setAuthEmail(email);
            authEmail.setAuthCode(authCode);
            authRepository.save(authEmail);
        }
        return authCode;
    }

    @Override
    public AuthEmail getByAuthEmail(String email) {
       return authRepository.findByAuthEmail(email).orElse(null);
    }

    @Override
    public void deleteAuthEmail(String email) {
        AuthEmail authEmail = getByAuthEmail(email);
        authRepository.delete(authEmail);
    }

    @Override
    public Boolean existsByUserEmail(String userEmail) {
        return emailRepository.existsByUserEmail(userEmail);
    }

    // 인증코드 이메일 전송
    @Override
    public Boolean sendAuthEmail(String userEmail, String authCode) {
        String mailTitle = "[공습] 이메일 인증코드 안내";
        String mailContent = String.format("<div>발급된 인증코드는 <b>%s</b> 입니다.</div> <div>위 인증코드를 회원가입 인증코드 입력창에 정확하게 입력해주세요.</div>", authCode);
        return mailUtil.sendEmail(userEmail, mailTitle, mailContent);
    }


}
