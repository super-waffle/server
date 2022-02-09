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
        for (Integer userSeq : userList) {d
            if (sseEmitters.containsKey(userSeq)) {
                SseEmitter sseEmitter = sseEmitters.get(userSeq);
                try {
//                    System.out.println(userSeq +"한테"+meetingTitle + " "  + meetingSeq + "보내는중");
                    sseEmitter.send(SseEmitter.event().name("MeetingVacancy").data(meetingSeq + "_" + meetingTitle));
                } catch (Exception e) {
//                    System.out.println("근데 에러남");
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
