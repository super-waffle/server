package com.gongsp.api.controller;

import com.gongsp.api.request.user.*;
import com.gongsp.api.response.study.StudyDetailInfoGetRes;
import com.gongsp.api.response.user.ApplicantsListGetRes;
import com.gongsp.api.response.user.MyMeetingGetRes;
import com.gongsp.api.response.user.OtherUserProfileGetRes;
import com.gongsp.api.response.user.UserProfileGetRes;
import com.gongsp.api.response.user.my_study.MyStudyListGetRes;
import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.api.service.SseService;
import com.gongsp.api.service.UserService;
import com.gongsp.common.auth.GongUserDetails;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import com.gongsp.db.entity.OtherUserProfile;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private int getUserSeqFromAuthentication(Authentication authentication) {
        // Token에 따른 사용자 인증 객체 내부의 사용자 정보를 가져온다
        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        // 사용자 정보 내부의 사용자 일련번호를 가져온다.
        return userDetails.getUserSeq();
    }

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SseService sseService;

    // API U-001
    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> getMyProfile(Authentication authentication) {
        int userSeq = getUserSeqFromAuthentication(authentication);

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
    @PatchMapping("")
    public ResponseEntity<BaseResponseBody> updateUserProfile(Authentication authentication,
                                                              @ModelAttribute UserInfoPatchReq infoPatchReq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<User> userInfo = userService.getUserByUserSeq(userSeq);

        if (!userInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such User"));

        User user = userInfo.get();

        userService.updateUserProfile(user, infoPatchReq);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-004
    @PatchMapping("/password")
    public ResponseEntity<BaseResponseBody> updateUserPassword(Authentication authentication,
                                                               @RequestBody UserPasswordPatchReq passwordPatchReq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<User> userInfo = userService.getUserByUserSeq(userSeq);

        if (!userInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such User"));

        User user = userInfo.get();

        if (!passwordEncoder.matches(passwordPatchReq.getPassword(), user.getUserPassword()))
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized"));

        userService.updateUserPassword(user, passwordEncoder.encode(passwordPatchReq.getNewPassword()));

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-005
    @PatchMapping("/nickname")
    public ResponseEntity<BaseResponseBody> updateUserTimeGoal(Authentication authentication,
                                                               @RequestBody UserNicknamePatchReq nickname) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        if (userService.updateUserNickname(userSeq, nickname.getNickname()))
            return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
        return ResponseEntity.ok(BaseResponseBody.of(404, "No Such User"));
    }

    // API U-006
    @GetMapping("/studies")
    public ResponseEntity<? extends BaseResponseBody> getStudiesList(Authentication authentication) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<List<StudyRes>> studies = userService.getUserIncludedStudies(userSeq);

        if (studies.isPresent())
            return ResponseEntity.ok(MyStudyListGetRes.of(200, "Success", studies.get()));
        return ResponseEntity.ok(MyStudyListGetRes.of(404, "No Study List", null));
    }

    // API U-007
    @GetMapping("/studies/{studySeq}")
    public ResponseEntity<? extends BaseResponseBody> getDetailStudyInfo(@PathVariable(value = "studySeq") int studySeq) {
        Optional<StudyRes> studyInfo = userService.getUserIncludedDetailStudyInfo(studySeq);

        if (studyInfo.isPresent())
            return ResponseEntity.ok(StudyDetailInfoGetRes.of(200, "Success", studyInfo.get()));
        return ResponseEntity.ok(StudyDetailInfoGetRes.of(404, "No Such Study", null));
    }

    // API U-008
    @PatchMapping("/studies/{studySeq}")
    public ResponseEntity<? extends BaseResponseBody> updateStudyInfo(Authentication authentication,
                                                                      @PathVariable(value = "studySeq") int studySeq,
                                                                      @RequestBody UserStudyUpdatePatchReq studyPatchInfo) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.patchStudyInfo(study, studyPatchInfo);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-009-1
    @PatchMapping("/studies/{studySeq}/end")
    public ResponseEntity<BaseResponseBody> endStudy(Authentication authentication, @PathVariable(value = "studySeq") int studySeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.endStudy(study);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-009-2
    @PatchMapping("studies/{studySeq}/quit")
    public ResponseEntity<BaseResponseBody> quitStudy(Authentication authentication, @PathVariable(value = "studySeq") int studySeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() == userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are The Host"));

        userService.quitStudy(userSeq, study);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-010
    @PatchMapping("studies/{studySeq}/start")
    public ResponseEntity<BaseResponseBody> startStudy(Authentication authentication, @PathVariable(value = "studySeq") int studySeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.startStudy(userSeq, study);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-011
    @PatchMapping("studies/{studySeq}/recruit-end")
    public ResponseEntity<BaseResponseBody> endStudyRecruit(Authentication authentication, @PathVariable(value = "studySeq") int studySeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.endStudyRecruit(study);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-012
    @GetMapping("studies/{studySeq}/applicants")
    public ResponseEntity<? extends BaseResponseBody> getStudyApplicants(Authentication authentication, @PathVariable(value = "studySeq") int studySeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        Optional<Collection<User>> applicants = userService.getApplicantByStudySeq(studySeq);

        if (!applicants.isPresent() || applicants.get().isEmpty())
            return ResponseEntity.ok(ApplicantsListGetRes.of(204, "No List Exist", null));
        return ResponseEntity.ok(ApplicantsListGetRes.of(200, "Success", applicants.get()));
    }

    // API U-013
    @PostMapping("/studies/{studySeq}/applicants/{applicantSeq}")
    public ResponseEntity<BaseResponseBody> grantApplicant(Authentication authentication,
                                                           @PathVariable(value = "studySeq") int studySeq,
                                                           @PathVariable(value = "applicantSeq") int applicantSeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.grantApplicant(studySeq, applicantSeq);

        sseService.sendStudyGrantNotice(applicantSeq, studySeq, study.getTitle());

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-014
    @DeleteMapping("/studies/{studySeq}/applicants/{applicantSeq}")
    public ResponseEntity<BaseResponseBody> rejectApplicant(Authentication authentication,
                                                            @PathVariable(value = "studySeq") int studySeq,
                                                            @PathVariable(value = "applicantSeq") int applicantSeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.rejectApplicant(studySeq, applicantSeq);

        sseService.sendStudyRejectNotice(applicantSeq, studySeq, study.getTitle());

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-015
    @DeleteMapping("/studies/{studySeq}/kick/{kickSeq}")
    public ResponseEntity<BaseResponseBody> kickMember(Authentication authentication,
                                                       @PathVariable(value = "studySeq") int studySeq,
                                                       @PathVariable(value = "kickSeq") int kickSeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Study> studyInfo = userService.getStudyInfo(studySeq);

        if (!studyInfo.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Study"));

        Study study = studyInfo.get();

        if (study.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.kickMember(studySeq, kickSeq);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-016
    @GetMapping("/meetings")
    public ResponseEntity<? extends BaseResponseBody> getMyMeetingRoom(Authentication authentication) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Meeting> meeting = userService.getMyMeetingRoomInfo(userSeq);

        if (!meeting.isPresent())
            return ResponseEntity.ok(MyMeetingGetRes.of(404, "No MeetingRoom Info", null));

        return ResponseEntity.ok(MyMeetingGetRes.of(200, "Success", meeting.get()));
    }

    // API U-017
    @PatchMapping("/meetings/{meetingSeq}")
    public ResponseEntity<BaseResponseBody> patchMyMeetingRoom(Authentication authentication,
                                                               @ModelAttribute UserMeetingPatchReq meetingPatchReq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Meeting> meeting = userService.getMyMeetingRoomInfo(userSeq);

        if (!meeting.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Meeting Info"));

        Meeting meetingInfo = meeting.get();

        if (meetingInfo.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.updateMeetingInfo(meetingInfo, meetingPatchReq);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    // API U-018
    @DeleteMapping("/meetings/{meetingSeq}")
    public ResponseEntity<BaseResponseBody> deleteMyMeetingRoom(Authentication authentication,
                                                                @PathVariable(value = "meetingSeq") int meetingSeq) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<Meeting> meeting = userService.getMyMeetingRoomInfo(userSeq);

        if (!meeting.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No Such Meeting Info"));

        Meeting meetingInfo = meeting.get();

        if (meetingInfo.getHostSeq() != userSeq)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Not Authorized : You Are Not The Host"));

        userService.deleteMeeting(meetingInfo);

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }
}
