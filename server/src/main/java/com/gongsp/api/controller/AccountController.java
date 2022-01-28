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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> signup(@RequestBody AccountSignupPostReq signupInfo) {
        signupInfo.setPassword(passwordEncoder.encode(signupInfo.getPassword()));
        User user = accountService.createUser(signupInfo);
        // 사용자 생성 여부 확인
        if (user == null) {
            return ResponseEntity.ok(BaseResponseBody.of(404, "Signup Failed"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(200, "Signup Successful"));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<AccountLoginPostRes> login(@RequestBody AccountLoginPostReq loginInfo) {
        String userEmail = loginInfo.getEmail();
        String userPassword = loginInfo.getPassword();

        User user = accountService.getUserByUserEmail(userEmail);
        // 존재하지 않는 사용자
        if (user == null) {
            return ResponseEntity.ok(AccountLoginPostRes.of(404, "User Not Found", null));
        }
        // 존재하는 사용자
        if (passwordEncoder.matches(userPassword, user.getUserPassword())) {
            // 로그인 성공
            return ResponseEntity.ok(AccountLoginPostRes.of(200, "Login Successful", JwtTokenUtil.getToken(user.getUserSeq())));
        }
        return ResponseEntity.ok(AccountLoginPostRes.of(401, "Login Failed", null));
    }

    // 닉네임 중복검사
    @PostMapping("/nickname")
    public ResponseEntity<? extends BaseResponseBody> checkNickname(@RequestBody AccountCheckNicknamePostReq nicknameInfo) {
        // DB에 체크
        if (accountService.existsByUserNickname(nicknameInfo.getNickname())) {
            return ResponseEntity.ok(BaseResponseBody.of(409, "Nickname Occupied"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(200, "Nickname Available"));
    }

    // 비밀번호 찾기
    @PostMapping("/password")
    public ResponseEntity<? extends BaseResponseBody> findPassword(@RequestBody AccountFindPasswordPostReq emailInfo) {
        // DB에서 사용자 조회
        User user = accountService.getUserByUserEmail(emailInfo.getEmail());
        // 없는 경우
        if (user == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Cannot find user with this email."));
        }
        // 있는 경우
        // 임시 비밀번호 생성
        String tempPassword = accountService.createTempPassword();
        // 임시 비밀번호 갱신
        String encodedTempPassword = passwordEncoder.encode(tempPassword);
        Boolean updatePassword = accountService.updateTempPassword(emailInfo.getEmail(), encodedTempPassword);
        // 이메일 전송
        Boolean emailSent = accountService.sendPasswordEmail(emailInfo.getEmail(), tempPassword);
        if (updatePassword && emailSent) {
            // 이메일 성공적 전송
            return ResponseEntity.ok(BaseResponseBody.of(200, "Successfully sent email."));
        }
        return ResponseEntity.ok(BaseResponseBody.of(409, "Failed to send email."));
    }
}
