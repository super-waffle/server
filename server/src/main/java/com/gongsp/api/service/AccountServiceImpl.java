package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountCheckNicknamePostReq;
import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Autowired
    private Environment env;

    @Override
    public User createUser(AccountSignupPostReq signupInfo) {
        User user = new User();
        user.setUserEmail(signupInfo.getEmail());
        user.setUserNickname(signupInfo.getNickname());
        user.setUserPassword(signupInfo.getPassword());
        accountRepository.save(user);
        return accountRepository.findUserByUserEmail(signupInfo.getEmail()).get();
    }

    @Override
    public User getUserByUserEmail(String userEmail) {
        return accountRepository.findUserByUserEmail(userEmail).orElse(null);
    }

    @Override
    public Boolean existsByUserNickname(AccountCheckNicknamePostReq nicknameInfo) {
        return accountRepository.existsByUserNickname(nicknameInfo.getNickname());
    }

    // 임시 비밀번호 이메일 전송
    @Override
    public Boolean updateTempPassword(String userEmail, String uuid, String tempPassword) {

        // 암호화시킨 비밀번호 저장
        User user = accountRepository.findUserByUserEmail(userEmail).orElse(null);
        user.setUserPassword(tempPassword);
        accountRepository.save(user);

        // 이메일 발송
        String sendTo = userEmail;
        String mailTitle = "[공습] 임시 비밀번호 발급 안내";
        String mailContent = String.format("<div>발급된 임시 비밀번호는 [%s] 입니다.</div> <div>로그인 후, 비밀번호를 변경해주세요.</div>", uuid);

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                message.setTo(sendTo);
                message.setFrom(sendFrom);
                message.setSubject(mailTitle);
                message.setText(mailContent, true);     // true: html 형식 사용

//                // Mail에 img 삽입
//                ClassPathResource resource = new ClassPathResource("img주소/img이름.png");
//                message.addlnline("img", resource.getFile());
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