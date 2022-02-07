package com.gongsp.api.controller;

import com.gongsp.api.request.meeting.MeetingCreatePostReq;
import com.gongsp.api.request.study.StudyCreatePostReq;
import com.gongsp.api.request.study.StudyParameter;
import com.gongsp.api.response.meeting.MeetingDetailGetRes;
import com.gongsp.api.response.study.StudyDetailGetRes;
import com.gongsp.api.response.study.StudyEnterPostRes;
import com.gongsp.api.response.study.StudyListGetRes;
import com.gongsp.api.response.study.StudyRes;
import com.gongsp.api.service.*;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudyApplyId;
import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.entity.StudyRoom;
import io.openvidu.java.client.OpenVidu;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
    StudyMemberService studyMemberService;
    @Autowired
    LogTimeService logTimeService;
    @Autowired
    StudyHistoryService studyHistoryService;
    @Autowired
    StudyDayService studyDayService;
    @Autowired
    StudyApplyService studyApplyService;

    public StudyController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    //스터디룸 입실
    @PostMapping("/{study-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> enterStudy(@PathVariable("study-seq") Integer studySeq, Authentication authentication){

        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());

        // study -> studyRoom



        // 존재하는 스터디룸만 입실 할 수 있음
        Optional<StudyRoom> opStudy = studyRoomService.getStudy(studySeq);
        if (!opStudy.isPresent())
            return ResponseEntity.ok(StudyEnterPostRes.of(404, "Fail : Not valid studySeq"));

        System.out.println(opStudy.get());

        // 권한없는 사용자가 입실시도 할 경우
        if(!studyMemberService.existsMember(userSeq, studySeq))
            return ResponseEntity.ok(StudyEnterPostRes.of(406, "Fail : User don't have permission"));

        // 일시방출당한 사용자가 입실시도 할 경우
        if(!studyHistoryService.validMemberToday(userSeq, studySeq, LocalDate.now()))
            return ResponseEntity.ok(StudyEnterPostRes.of(406, "Fail : User was ejected"));

        // 스터디 정원은 생각 안하겠습니당 신청받을때 무조건 6명 이하로 받는다고 생각해서...

//        // 이미 방에 들어가 있는경우 칼럼들 안넣고 토큰발급 다시
//        if(studyHistoryService.existsMemberToday(userSeq, studySeq, LocalDate.now()))
//            return ResponseEntity.ok(StudyEnterPostRes.of(406, "Fail : Already exists in study room"));

        StudyRoom study = opStudy.get();
        // token 얻기 (session 생성 후 connection 생성)
        String token = studyRoomService.getToken(openVidu, userSeq, study);

        //tb_history_study 칼럼추가
//        studyHistoryService.createHistory(userSeq, studySeq, study.ge);
//
//        //당일 최초로 공부를 시작한 사용자
//        if (!logTimeService.existsLog(userSeq, LocalDate.now())) {
////            System.out.println("공부기록삽입");
//            logTimeService.createLogTime(userSeq);
//        }
//
//        if (token.equals("InternalError"))
//            return ResponseEntity.ok(MeetingEnterPostRes.of(409, "Fail : OpenViduJavaClientException", null));
//
//        if (token.equals("GenError"))
//            return ResponseEntity.ok(MeetingEnterPostRes.of(409, "Fail : Generate meeting room", null));
//
//        return ResponseEntity.ok(MeetingEnterPostRes.of(200, "Success : Enter study room", token, study, isHost, isLate));

        return ResponseEntity.ok(StudyEnterPostRes.of(200, "Success : Enter study room"));
    }
    //스터디룸 퇴실

    //일시방출
    //일시방출 누적횟수 확인
    //스터디원 일시방출하기

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
        if(!opStudyDays.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Get study days"));
        System.out.println(Arrays.toString(opStudyDays.get()));
        return ResponseEntity.ok(StudyDetailGetRes.of(200, "Success : Get study detail", studyRoom, studyRoomService.getStudyMemberNum(studySeq), opStudyDays.get()));
    }

    // 스터디 게시물 작성 = 스터디룸 생성
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody> createStudy(@RequestBody StudyCreatePostReq studyCreatePostReq, Authentication authentication) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        StudyRoom studyRoom =studyRoomService.createStudy(studyCreatePostReq, userSeq);
        if (studyRoom == null)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Create study room"));
        studyDayService.createStudyDays(studyCreatePostReq.getDay(), studyRoom.getStudySeq());
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Create study room"));
    }

    // 스터디 신청
    @PostMapping("{study-seq}/application")
    public ResponseEntity<? extends BaseResponseBody> applyStudy(@PathVariable("study-seq") Integer studySeq, Authentication authentication){
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        if(studyApplyService.existsStudyById(new StudyApplyId(userSeq, studySeq)))
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Already applied"));
        studyApplyService.createApplicant(new StudyApplyId(userSeq, studySeq));
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Apply study"));
    }
}
