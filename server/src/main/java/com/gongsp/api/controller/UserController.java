package com.gongsp.api.controller;

import com.gongsp.api.response.user.OtherUserProfileGetRes;
import com.gongsp.api.response.user.UserProfileGetRes;
import com.gongsp.api.service.UserService;
import com.gongsp.common.auth.GongUserDetails;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.OtherUserProfile;
import com.gongsp.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // API U-001
    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> getMyProfile(Authentication authentication) {
        // Token에 따른 사용자 인증 객체 내부의 사용자 정보를 가져온다
        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        // 사용자 정보 내부의 사용자 일련번호를 가져온다.
        int userSeq = userDetails.getUserSeq();
        // 사용자 일련번호를 통해 사용자의 모든 정보를 가져온다.
        Optional<User> userInfo = userService.getUserByUserSeq(userSeq);

        if (userInfo.isPresent())
            return ResponseEntity.ok(UserProfileGetRes.of(200, "Success", userInfo.get()));

        return ResponseEntity.ok(UserProfileGetRes.of(404, "No User Info", null));
    }

    // API U-002
    @GetMapping("/{userSeq}")
    public ResponseEntity<? extends BaseResponseBody> getOtherProfile(Authentication authentication, @PathVariable(value = "userSeq") int userSeq) {
        Optional<OtherUserProfile> userInfo = userService.getOtherProfile(userSeq);

        if (userInfo.isPresent())
            return ResponseEntity.ok(OtherUserProfileGetRes.of(200, "Success", userInfo.get()));

        return ResponseEntity.ok(OtherUserProfileGetRes.of(404, "No User Profile Info", null));
    }
}
