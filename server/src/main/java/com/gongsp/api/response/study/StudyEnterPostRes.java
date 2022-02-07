package com.gongsp.api.response.study;

import com.gongsp.api.response.meeting.MeetingEnterPostRes;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudyRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private LocalTime studyEnterTime; //입실시간

    public static StudyEnterPostRes of(Integer statusCode, String message, String sessionToken) {
        StudyEnterPostRes res = new StudyEnterPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setSessionToken(sessionToken);
        res.setStudyDate(LocalDate.now());
        res.setStudyEnterTime(LocalTime.now());
        return res;
    }

    public static StudyEnterPostRes of(Integer statusCode, String message, String sessionToken, StudyRoom study, Boolean isHost, Boolean isLate) {
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
        return res;
    }
}
