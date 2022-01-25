package com.gongsp.api.controller;

import com.gongsp.api.service.UserService;
import com.gongsp.common.model.response.BaseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> getMyProfile(Authentication authentication) throws Exception {
        return null; //ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원가입 성공"));
    }
}
