package com.gongsp.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("sseService")
public class SseServiceImpl implements SseService {

    public static Map<Integer, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    public void sendMeetingVacancyNotice(List<Integer> userList, Integer meetingSeq, String meetingTitle) {
        for (Integer userSeq : userList) {
            if (sseEmitters.containsKey(userSeq)) {
                SseEmitter sseEmitter = sseEmitters.get(userSeq);
                try {
                    sseEmitter.send(SseEmitter.event().name("MeetingVacancy").data(meetingTitle).id(meetingSeq.toString()));
                } catch (Exception e) {
                    sseEmitters.remove(userSeq);
                }
            }
        }
    }

    @Override
    public void register(SseEmitter sseEmitter, Integer userSeq) {
        //userSeq를 key로해서 SseEmitter 저장
        sseEmitters.put(userSeq, sseEmitter);
        sseEmitter.onCompletion(() -> sseEmitters.remove(userSeq));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userSeq));
        sseEmitter.onError((e) -> sseEmitters.remove(userSeq));
    }
}
