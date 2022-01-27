package com.gongsp.api.controller;

import com.gongsp.api.request.email.EmailAuthPostReq;
import com.gongsp.api.service.EmailService;
import com.gongsp.common.model.response.BaseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 이메일 인증 요청
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> emailCert(@RequestBody EmailAuthPostReq emailInfo) {
        // 기존 사용자 여부
        if (emailService.existsByUserEmail(emailInfo.getEmail())) {
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Email Unavailable"));
        }
        // 인증코드 생성/변경 및 저장
        String authCode = emailService.createAuthCode(emailInfo.getEmail());
        // 이메일 전송
        Boolean emailSent = emailService.sendAuthEmail(emailInfo.getEmail(), authCode);
        if (emailSent) {
            // 이메일 성공적 전송
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Email Sent"));
        }
        return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Failed to send email"));
    }
}
