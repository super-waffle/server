package com.gongsp.api.controller;

import com.gongsp.api.request.account.AccountLoginPostReq;
import com.gongsp.api.request.account.AccountRegisterPostReq;
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
    PasswordEncoder passwordEncoder;

    @PostMapping("")
    public ResponseEntity<? extends BaseResponseBody> create(@RequestBody AccountRegisterPostReq accountRegisterPostReq) throws Exception {
        accountRegisterPostReq.setUserPassword(passwordEncoder.encode(accountRegisterPostReq.getUserPassword()));
        accountService.createUser(accountRegisterPostReq);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<AccountLoginPostRes> login(@RequestBody AccountLoginPostReq loginInfo) {
        // 사용자가 입력한 id, password
        String userEmail = loginInfo.getUserEmail();
        String password = loginInfo.getUserPassword();

        // 사용자가 입력한 id를 통해서 User객체를 DB에서 조회해 얻음
        User user = accountService.getUserByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(404).body(AccountLoginPostRes.of(404, "존재하지 않는 사용자", null));
        }

        // 비밀번호 복호화 후 유효한 패스워드인지 여부 확인
        if (passwordEncoder.matches(password, user.getUserPassword())) {
            // 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
            return ResponseEntity.ok(AccountLoginPostRes.of(200, "로그인 성공", JwtTokenUtil.getToken(user.getUserSeq())));
        }
        // 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
        return ResponseEntity.status(401).body(AccountLoginPostRes.of(401, "일치하지 않은 비밀번호", null));
    }
}
