package com.gongsp.api.controller;

import com.gongsp.api.request.account.AccountCheckNicknamePostReq;
import com.gongsp.api.request.account.AccountFindPasswordPostReq;
import com.gongsp.api.request.account.AccountLoginPostReq;
import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.api.response.account.AccountLoginPostRes;
import com.gongsp.api.service.AccountService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.common.util.JwtTokenUtil;
import com.gongsp.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String sendFrom;

    @Autowired
    Environment env;

    // 회원가입
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> signup(@RequestBody AccountSignupPostReq signupInfo) {
        signupInfo.setPassword(passwordEncoder.encode(signupInfo.getPassword()));
        accountService.createUser(signupInfo);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Signup Successful"));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<AccountLoginPostRes> login(@RequestBody AccountLoginPostReq loginInfo) {
        String userEmail = loginInfo.getEmail();
        String userPassword = loginInfo.getPassword();

        User user = accountService.getUserByUserEmail(userEmail);
        // 존재하지 않는 사용자
        if (user == null) {
            return ResponseEntity.status(404).body(AccountLoginPostRes.of(404, "User Not Found", null));
        }
        // 존재하는 사용자
        if (passwordEncoder.matches(userPassword, user.getUserPassword())) {
            // 로그인 성공
            return ResponseEntity.ok(AccountLoginPostRes.of(200, "Login Successful", JwtTokenUtil.getToken(user.getUserSeq())));
        }
        return ResponseEntity.status(401).body(AccountLoginPostRes.of(401, "Login Failed", null));
    }

    // 닉네임 중복검사
    @PostMapping("/nickname")
    public ResponseEntity<? extends BaseResponseBody> checkNickname(@RequestBody AccountCheckNicknamePostReq nicknameInfo) {
        // DB에 체크
        if (accountService.existsByUserNickname(nicknameInfo)) {
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Nickname Occupied"));
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Nickname Available"));
    }

    // 임시 비밀번호 이메일 전송
   public Boolean sendMail(String userEmail) {
        // 임시 비밀번호 담기
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        String tempPassword = passwordEncoder.encode(uuid);
        accountService.updateTempPassword(userEmail, tempPassword); // 암호화시킨 비밀번호 저장

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


    // 비밀번호 찾기
    @PostMapping("/password")
    public ResponseEntity<? extends BaseResponseBody> findPassword(@RequestBody AccountFindPasswordPostReq emailInfo) {
        // DB에서 사용자 조회
        User user = accountService.getUserByUserEmail(emailInfo.getEmail());
        // 없는 경우
        if (user == null) {
            return ResponseEntity.status(403).body(BaseResponseBody.of(403, "Cannot find user with this email."));
        }
        // 있는 경우
        Boolean emailSent = sendMail(emailInfo.getEmail());
        if (emailSent) {
            // 이메일 성공적 전송
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Successfully sent email."));
        }
        return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Failed to send email."));
    }
}
