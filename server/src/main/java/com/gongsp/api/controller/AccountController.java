package com.gongsp.api.controller;

import com.gongsp.api.request.account.AccountCheckNicknamePostReq;
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
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        if (accountService.getUserByUserNickname(nicknameInfo)) {
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Nickname Occupied"));
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Nickname Available"));
    }
}
