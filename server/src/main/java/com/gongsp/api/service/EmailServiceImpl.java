package com.gongsp.api.service;

import com.gongsp.common.util.UuidUtil;
import com.gongsp.common.util.MailUtil;
import com.gongsp.db.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UuidUtil uuidUtil;
    
    // 인증코드 발급
    @Override
    public String createAuthCode() {
        return uuidUtil.createRandomUUID();
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
