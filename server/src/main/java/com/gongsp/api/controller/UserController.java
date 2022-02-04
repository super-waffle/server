package com.gongsp.api.controller;

import com.gongsp.api.request.user.UserTimeGoalPatchReq;
import com.gongsp.api.response.study.StudyDetailInfoGetRes;
import com.gongsp.api.response.user.OtherUserProfileGetRes;
import com.gongsp.api.response.user.UserProfileGetRes;
import com.gongsp.api.response.user.my_study.MyStudyListGetRes;
import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.api.service.UserService;
import com.gongsp.common.auth.GongUserDetails;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.OtherUserProfile;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // API U-003
    // API U-004

    // API U-005
    @PatchMapping("/goal")
    public ResponseEntity<BaseResponseBody> updateUserTimeGoal(Authentication authentication, @RequestBody UserTimeGoalPatchReq timeGoal) {
        // Token에 따른 사용자 인증 객체 내부의 사용자 정보를 가져온다
        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        // 사용자 정보 내부의 사용자 일련번호를 가져온다.
        int userSeq = userDetails.getUserSeq();

        if (userService.updateUserTimeGoal(userSeq, timeGoal.getTimeGoal()))
            return ResponseEntity.ok(BaseResponseBody.of(200,"Success"));
        return ResponseEntity.ok(BaseResponseBody.of(404, "No Such User"));
    }

    // API U-006
    @GetMapping("/studies")
    public ResponseEntity<? extends BaseResponseBody> getStudiesList(Authentication authentication) {
        // Token에 따른 사용자 인증 객체 내부의 사용자 정보를 가져온다
        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        // 사용자 정보 내부의 사용자 일련번호를 가져온다.
        int userSeq = userDetails.getUserSeq();

        Optional<List<StudyRes>> studies = userService.getUserIncludedStudies(userSeq);

        if (studies.isPresent())
            return ResponseEntity.ok(MyStudyListGetRes.of(200, "Success", studies.get()));
        return ResponseEntity.ok(MyStudyListGetRes.of(404, "No Study List", null));
    }

    // API U-007
    @GetMapping("/studies/{studySeq}")
    public ResponseEntity<? extends BaseResponseBody> getDetailStudyInfo(Authentication authentication, @PathVariable(value = "studySeq") int studySeq) {
        // Token에 따른 사용자 인증 객체 내부의 사용자 정보를 가져온다
        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        // 사용자 정보 내부의 사용자 일련번호를 가져온다.
        int userSeq = userDetails.getUserSeq();

        Optional<StudyRes> studyInfo = userService.getUserIncludedDetailStudyInfo(studySeq, userSeq);

        if (studyInfo.isPresent())
            return ResponseEntity.ok(StudyDetailInfoGetRes.of(200, "Success", studyInfo.get()));
        return ResponseEntity.ok(StudyDetailInfoGetRes.of(404, "No Such Study", null));
    }
}
