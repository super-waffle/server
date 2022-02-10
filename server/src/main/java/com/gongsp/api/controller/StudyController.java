package com.gongsp.api.controller;

import com.gongsp.api.request.study.StudyCreatePostReq;
import com.gongsp.api.request.study.StudyExitPatchReq;
import com.gongsp.api.request.study.StudyParameter;
import com.gongsp.api.response.study.StudyBanPostRes;
import com.gongsp.api.response.study.StudyDetailGetRes;
import com.gongsp.api.response.study.StudyEnterPostRes;
import com.gongsp.api.response.study.StudyListGetRes;
import com.gongsp.api.service.*;
import com.gongsp.common.auth.GongUserDetails;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.*;
import io.openvidu.java.client.OpenVidu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/studies")
public class StudyController {
    // OpenVidu object as entrypoint of the SDK
    private OpenVidu openVidu;

    // URL where our OpenVidu server is listening
    private String OPENVIDU_URL;

    // Secret shared with our OpenVidu server
    private String SECRET;

    @Autowired
    StudyRoomService studyRoomService;
    @Autowired
    StudyRoomMemberService studyMemberService;
    @Autowired
    LogTimeService logTimeService;
    @Autowired
    StudyHistoryService studyHistoryService;
    @Autowired
    StudyDayService studyDayService;
    @Autowired
    StudyApplyService studyApplyService;
    @Autowired
    UserService userService;
    @Autowired
    SseService sseService;

    public StudyController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    //스터디룸 입실
    @PostMapping("/{study-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> enterStudy(@PathVariable("study-seq") Integer studySeq, Authentication authentication) {

        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());

        // 존재하는 스터디룸만 입실 할 수 있음
        Optional<StudyRoom> opStudyRoom = studyRoomService.getStudyRoom(studySeq);
        if (!opStudyRoom.isPresent())
            return ResponseEntity.ok(StudyEnterPostRes.of(404, "Fail : Not valid studySeq"));

        // 권한없는 사용자가 입실시도 할 경우
        if (!studyMemberService.existsMember(userSeq, studySeq))
            return ResponseEntity.ok(StudyEnterPostRes.of(406, "Fail : User don't have permission"));

        LocalDate curDate = LocalDate.now();

        // 일시방출당한 사용자가 입실시도 할 경우
        if (!studyHistoryService.validMemberToday(userSeq, studySeq, curDate))
            return ResponseEntity.ok(StudyEnterPostRes.of(406, "Fail : User was ejected"));

        // 스터디 시작 전/ 종료 후
//        if (!studyDayService.isValidTime(studySeq, curDate, LocalTime.now()))
//            return ResponseEntity.ok(StudyEnterPostRes.of(407, "Fail : Study has not started yet or already ended"));

        // 스터디 정원은 생각 안하겠습니당 신청받을때 무조건 6명 이하로 받는다고 생각해서...

        StudyRoom studyRoom = opStudyRoom.get();
        // token 얻기 (session 생성 후 connection 생성)
        String token = studyRoomService.getToken(openVidu, userSeq, studyRoom);

        studyMemberService.updateMemberOnair(userSeq, studySeq, true);

//        if (!studyHistoryService.existsAnyoneToday(studySeq, curDate)) {
//            studyRoomService.updateStudyOnair(studySeq, true);
//        }
        studyRoomService.updateStudyOnair(studySeq, studyMemberService.getStudyOnairCnt(studySeq));

        boolean isLate;
        if (!studyHistoryService.existsMemberToday(userSeq, studySeq, curDate)) {
            isLate = studyHistoryService.createHistory(userSeq, studySeq, false);
            //당일 최초로 공부를 시작한 사용자
            if (!logTimeService.existsLog(userSeq, curDate)) {
                logTimeService.createLogTime(userSeq);
            }
        } else {
            isLate = studyHistoryService.isMemberLate(userSeq, studySeq, curDate);
        }

        if (token.equals("InternalError"))
            return ResponseEntity.ok(StudyEnterPostRes.of(409, "Fail : OpenViduJavaClientException", null));
        if (token.equals("GenError"))
            return ResponseEntity.ok(StudyEnterPostRes.of(409, "Fail : Generate meeting room", null));
        return ResponseEntity.ok(StudyEnterPostRes.of(200, "Success : Enter study room", token, studyRoom, studyRoom.getHost().getUserSeq().equals(userSeq), isLate));
    }

    //스터디룸 퇴실
    @PatchMapping("/{study-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> removeUser(@PathVariable("study-seq") Integer studySeq, @RequestBody StudyExitPatchReq studyExitPatchReq, Authentication authentication) {
        if(authentication == null)
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access denied"));
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());

        String sessionName = studyRoomService.getStudyUrl(studySeq);
        if(sessionName == null)
            return ResponseEntity.ok(BaseResponseBody.of(408, "Fail : Not valid study room seq"));
        String token = studyExitPatchReq.getSessionToken();

        studyMemberService.updateMemberOnair(userSeq, studySeq, false);
        studyRoomService.updateStudyOnair(studySeq, studyMemberService.getStudyOnairCnt(studySeq));

        String result = studyRoomService.removeUser(sessionName, token, studySeq);

        if ("Error".equals(result))
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Remove user"));

        // 시간 넘어온거 누적
        logTimeService.updateStudyLogTime(userSeq, studyExitPatchReq.getLogStudy(), studyExitPatchReq.getLogStartTime());
        userService.updateUserLogTime(userSeq, studyExitPatchReq.getLogStudy());

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Remove user"));
    }

    //일시방출 누적횟수 확인
    @GetMapping("{study-seq}/ban/{user-seq}")
    public ResponseEntity<? extends BaseResponseBody> getBanCnt(@PathVariable("study-seq") Integer studySeq, @PathVariable("user-seq") Integer userSeq, Authentication authentication) {
        Optional<StudyRoomMember> opStudyMember = studyMemberService.getStudyMember(userSeq, studySeq);
        if (!opStudyMember.isPresent())
            return ResponseEntity.ok(StudyBanPostRes.of(409, "Fail : Not valid studySeq or userSeq", null));

        Integer hostSeq = Integer.parseInt((String) authentication.getPrincipal());
        if (!studyRoomService.getStudyRoom(studySeq).get().getHost().getUserSeq().equals(hostSeq))
            return ResponseEntity.ok(StudyBanPostRes.of(408, "Fail : Only can host", null));
        return ResponseEntity.ok(StudyBanPostRes.of(200, "Success : Get ban count", opStudyMember.get().getMemberEjectCount()));
    }

    //스터디원 일시방출하기
    @PatchMapping("{study-seq}/ban/{user-seq}")
    public ResponseEntity<? extends BaseResponseBody> banUserFromStudy(@PathVariable("study-seq") Integer studySeq, @PathVariable("user-seq") Integer userSeq, Authentication authentication) {
        if(authentication == null)
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access denied"));
        Optional<StudyRoomMember> opStudyMember = studyMemberService.getStudyMember(userSeq, studySeq);
        if (!opStudyMember.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Not valid studySeq or userSeq"));

        Integer hostSeq = Integer.parseInt((String) authentication.getPrincipal());
        StudyRoom studyRoom = studyRoomService.getStudyRoom(studySeq).get();
        if (!studyRoom.getHost().getUserSeq().equals(hostSeq))
            return ResponseEntity.ok(BaseResponseBody.of(408, "Fail : Only can host"));

        studyHistoryService.updateHistoryEjected(userSeq, studySeq, LocalDate.now(), true);

        studyMemberService.banMember(userSeq, studySeq);
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Ban user"));
    }

    // 스터디 게시물 목록 조회
    @GetMapping
    public ResponseEntity<? extends BaseResponseBody> getStudyList(StudyParameter studyParameter, Authentication authentication) {
        if (studyParameter.getPage() == null)
            return ResponseEntity.ok(StudyListGetRes.of(409, "Fail : Get Study List. No page"));
        if (studyParameter.getType() == null)
            return ResponseEntity.ok(StudyListGetRes.of(409, "Fail : Get Study List. No Category type"));
        return ResponseEntity.ok(StudyListGetRes.of(200, "Success : Get Study List", studyParameter, studyRoomService.getStudyCnt(studyParameter), studyRoomService.getStudyList(studyParameter, Integer.parseInt((String) authentication.getPrincipal()))));
    }

    // 스터디 게시물 상세조회
    @GetMapping("/{study-seq}")
    public ResponseEntity<? extends BaseResponseBody> getStudyDetail(@PathVariable("study-seq") Integer studySeq, Authentication authentication) {
        Optional<StudyRoom> opStudyRoom = studyRoomService.getStudyDetail(studySeq);
        if (!opStudyRoom.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Get study detail"));
        StudyRoom studyRoom = opStudyRoom.get();
        Optional<StudyDay[]> opStudyDays = studyDayService.getStudyDay(studySeq);
        if (!opStudyDays.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Get study days"));
        return ResponseEntity.ok(StudyDetailGetRes.of(200, "Success : Get study detail", studyRoom, studyRoomService.getStudyMemberNum(studySeq), opStudyDays.get()));
    }

    // 스터디 게시물 작성 = 스터디룸 생성
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody> createStudy(@RequestBody StudyCreatePostReq studyCreatePostReq, Authentication authentication) {
        if(studyCreatePostReq.getDay().size() == 0)
            return ResponseEntity.ok(BaseResponseBody.of(408, "Fail : Don't select day"));
        if(studyCreatePostReq.getStudyTitle() == null || studyCreatePostReq.getStudyDesc()==null || studyCreatePostReq.getStudyShortDesc()==null)
            return ResponseEntity.ok(BaseResponseBody.of(407, "Fail : Not valid input"));
        if(studyCreatePostReq.getCategorySeq() == null)
            return ResponseEntity.ok(BaseResponseBody.of(406, "Fail : Not valid category"));
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        StudyRoom studyRoom = studyRoomService.createStudy(studyCreatePostReq, userSeq);
        if (studyRoom == null)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Don't saved"));
        studyDayService.createStudyDays(studyCreatePostReq.getDay(), studyRoom.getStudySeq());
        studyMemberService.createMember(userSeq, studyRoom.getStudySeq());
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Create study room"));
    }

    // 스터디 신청
    @PostMapping("{study-seq}/application")
    public ResponseEntity<? extends BaseResponseBody> applyStudy(@PathVariable("study-seq") Integer studySeq, Authentication authentication) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        Optional<StudyRoom> opStudyRoom = studyRoomService.getStudyRoom(studySeq);
        if (!opStudyRoom.isPresent())
            return ResponseEntity.ok(StudyEnterPostRes.of(404, "Fail : Not valid studySeq"));
        if (studyApplyService.existsStudyById(new StudyApplyId(userSeq, studySeq)))
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Already applied"));
        studyApplyService.createApplicant(new StudyApplyId(userSeq, studySeq));
        StudyRoom studyRoom = opStudyRoom.get();
        GongUserDetails gongUserDetails = (GongUserDetails) authentication.getDetails();
        sseService.sendStudyApplyNotice(studyRoom.getHost().getUserSeq(), studyRoom.getStudySeq(), ((GongUserDetails) authentication.getDetails()).getUsername(), studyRoom.getStudyTitle());
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Apply study"));
    }
}
