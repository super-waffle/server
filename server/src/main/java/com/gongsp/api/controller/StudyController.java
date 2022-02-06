package com.gongsp.api.controller;

import com.gongsp.common.model.response.BaseResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studies")
public class StudyController {
    //스터디룸 입실
    @PostMapping("/{study-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> enterStudy(){
        return ResponseEntity.ok();
    }
    //스터디룸 퇴실

    //일시방출
    //일시방출 누적횟수 확인
    //스터디원 일시방출하기

    //스터디 게시물 목록 조회
    //스터디 게시물 상세조회
    //스터디 게시물 작성 = 스터디룸 생성
    //스터디 신청
}
