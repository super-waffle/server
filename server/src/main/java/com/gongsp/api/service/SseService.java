package com.gongsp.api.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface SseService {
    void register(SseEmitter sseEmitter, Integer userSeq);
    void sendMeetingVacancyNotice(List<Integer> userList, Integer meetingSeq, String meetingTitle);
}
