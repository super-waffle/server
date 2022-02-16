package com.gongsp.api.service;

import com.gongsp.api.request.study.StudyCreatePostReq;
import com.gongsp.api.request.study.StudyParameter;
import com.gongsp.api.response.study.StudyRes;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudyRoom;
import io.openvidu.java.client.OpenVidu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyRoomService {
    Optional<StudyRoom> getStudyRoom(Integer studySeq);
    String getToken(OpenVidu openVidu, Integer userSeq, StudyRoom studyRoom);
    List<StudyRes> getStudyList(StudyParameter studyParameter, int userSeq);
    int getStudyMemberNum(Integer studySeq);
    int getStudyCnt(StudyParameter studyParameter);
    Optional<StudyRoom> getStudyDetail(Integer studySeq);
    StudyRoom createStudy(StudyCreatePostReq studyCreatePostReq, Integer userSeq);
    String getStudyUrl(Integer studySeq);
    void updateStudyOnair(Integer studySeq, int onairCnt);
    String removeUser(String sessionName, String token, Integer studySeq);
    void hideStudyRecruit(LocalDate date);
    List<Study> getRecruitEndedStudyList(LocalDate date);
}
