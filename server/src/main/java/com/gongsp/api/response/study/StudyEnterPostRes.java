package com.gongsp.api.response.study;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.StudyRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class StudyEnterPostRes extends BaseResponseBody {
    private String sessionToken;
    private Boolean isLate;
    private Integer studySeq;
    private Boolean isHost;
    private String studyTitle;
    private String studyShortDesc;
    private String studyDesc;
    private Integer studyCapacity;
    private LocalDate studyDate;      //시작날짜
    private LocalTime studyStartTime; //스터디 원래 시작시간
    private LocalTime studyEndTime; //스터디 원래 종료시간
    private LocalTime studyEnterTime; //입실시간
    private String userNickname;
    private String studyUrl;
    private Integer userSeq;
    private List<StudyMemberRes> memberList;

    public static StudyEnterPostRes of(Integer statusCode, String message, String sessionToken) {
        StudyEnterPostRes res = new StudyEnterPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setSessionToken(sessionToken);
        res.setStudyDate(LocalDate.now());
        res.setStudyEnterTime(LocalTime.now());
        return res;
    }

    public static StudyEnterPostRes of(Integer statusCode, String message, String sessionToken, StudyRoom study, Boolean isHost, Boolean isLate, String userNickname, Integer userSeq, List<StudyMemberRes>memberList, LocalTime studyStartTime, LocalTime studyEndTime) {
        StudyEnterPostRes res = new StudyEnterPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setSessionToken(sessionToken);
        res.setStudyDate(LocalDate.now());
        res.setStudyEnterTime(LocalTime.now());
        res.setIsLate(isLate);
        res.setStudySeq(study.getStudySeq());
        res.setIsHost(isHost);
        res.setStudyTitle(study.getStudyTitle());
        res.setStudyShortDesc(study.getStudyShortDesc());
        res.setStudyDesc(study.getStudyDesc());
        res.setStudyCapacity(study.getStudyCapacity());
        res.setUserNickname(userNickname);
        res.setStudyUrl(study.getStudyUrl());
        res.setUserSeq(userSeq);
        res.setMemberList(memberList);
        res.setStudyStartTime(studyStartTime);
        res.setStudyEndTime(studyEndTime);
        return res;
    }
}
