package com.gongsp.api.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface SseService {
    void register(SseEmitter sseEmitter, Integer userSeq);
    void sendMeetingVacancyNotice(List<Integer> userList, Integer meetingSeq, String meetingTitle);
    void sendStudyApplyNotice(Integer userSeq, Integer studySeq, String username, String studyTitle);
    void sendStudyGrantNotice(int applicantSeq, int studySeq, String title);
    void sendStudyRejectNotice(int applicantSeq, int studySeq, String title);
    void studyTimeNotice();
}
