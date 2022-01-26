package com.example.jpa.serverex.api.controller;

import com.example.jpa.serverex.api.request.AccountCheckNicknamePostReq;
import com.example.jpa.serverex.api.request.AccountSignupPostReq;
import com.example.jpa.serverex.api.request.UserLoginPostReq;
import com.example.jpa.serverex.api.response.UserLoginPostRes;
import com.example.jpa.serverex.api.service.UserService;
import com.example.jpa.serverex.common.model.response.BaseResponseBody;
import com.example.jpa.serverex.common.util.JwtTokenUtil;
import com.example.jpa.serverex.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> signup(@RequestBody AccountSignupPostReq signupInfo) {
        signupInfo.setPassword(passwordEncoder.encode(signupInfo.getPassword()));
        userService.createUser(signupInfo);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Signup Successful"));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginPostRes> login(@RequestBody UserLoginPostReq loginInfo) {
        String userEmail = loginInfo.getEmail();
        String userPassword = loginInfo.getPassword();

        User user = userService.getUserByUserEmail(userEmail);
        // 존재하지 않는 사용자
        if (user == null) {
            return ResponseEntity.status(404).body(UserLoginPostRes.of(404, "User Not Found", null));
        }
        // 존재하는 사용자
        if (passwordEncoder.matches(userPassword, user.getUserPassword())) {
            // 로그인 성공
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Login Successful", JwtTokenUtil.getToken(user.getUserSeq())));
        }
        return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Login Failed", null));
    }

    // 닉네임 중복검사
    @PostMapping("/nickname")
    public ResponseEntity<? extends BaseResponseBody> checkNickname(@RequestBody AccountCheckNicknamePostReq nicknameInfo) {
        // DB에 체크
        if (userService.getUserByUserNickname(nicknameInfo)) {
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Nickname Occupied"));
        }
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Nickname Available"));
    }
}