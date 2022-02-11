package com.gongsp.api.service;

import com.gongsp.db.entity.Notice;
import com.gongsp.db.repository.NoticeRepository;
import com.gongsp.db.repository.StudyRoomMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("sseService")
@EnableScheduling
public class SseServiceImpl implements SseService {

    public static Map<Integer, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Autowired
    StudyRoomMemberRepository studyRoomMemberRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @Override
    public void sendMeetingVacancyNotice(List<Integer> userList, Integer meetingSeq, String meetingTitle) {
        for (Integer userSeq : userList) {
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
            noticeRepository.save(new Notice(userSeq, 103, LocalDate.now(), "[" + meetingTitle + "] 자유열람실에 공석이 생겼습니다.", false));
        }
    }

    @Override
    public void sendStudyApplyNotice(Integer userSeq, Integer studySeq, String username, String studyTitle) {
        if (sseEmitters.containsKey(userSeq)) {
            SseEmitter sseEmitter = sseEmitters.get(userSeq);
            try {
                sseEmitter.send(SseEmitter.event().name("StudyApply").data(studySeq + "_" + username + "_" + studyTitle));
            } catch (Exception e) {
                sseEmitters.remove(userSeq);
            }
        }
        noticeRepository.save(new Notice(userSeq, 102, LocalDate.now(), "[" + studyTitle + "] 스터디에 ["+username+"] 회원이 참가를 신청하였습니다.", false));
    }

    @Override
    public void sendStudyGrantNotice(int userSeq, int studySeq, String title) {
        if (sseEmitters.containsKey(userSeq)) {
            SseEmitter sseEmitter = sseEmitters.get(userSeq);
            try {
                sseEmitter.send(SseEmitter.event().name("StudyGrant").data(studySeq + "_" + title));
            } catch (Exception e) {
                sseEmitters.remove(userSeq);
            }
        }
        noticeRepository.save(new Notice(userSeq, 102, LocalDate.now(), "[" + title + "] 스터디에 참가되었습니다.", false));
    }

    @Override
    public void sendStudyRejectNotice(int userSeq, int studySeq, String title) {
        if (sseEmitters.containsKey(userSeq)) {
            SseEmitter sseEmitter = sseEmitters.get(userSeq);
            try {
                sseEmitter.send(SseEmitter.event().name("StudyReject").data(studySeq + "_" + title));
            } catch (Exception e) {
                sseEmitters.remove(userSeq);
            }
        }
        noticeRepository.save(new Notice(userSeq, 102, LocalDate.now(), "[" + title + "] 스터디에 거절당했습니다.", false));
    }

    @Override
    @Scheduled(cron = "* */10 * * * *")
    public void studyTimeNotice() {
        List<Integer> userSeqs = studyRoomMemberRepository.findUserSeqByTime(LocalDate.now().getDayOfWeek().getValue(), LocalTime.now());
        if (userSeqs != null) {
            for (Integer userSeq : userSeqs) {
                if (sseEmitters.containsKey(userSeq)) {
                    SseEmitter sseEmitter = sseEmitters.get(userSeq);
                    try {
                        sseEmitter.send(SseEmitter.event().name("StudyTime").data("스터디 시작 10분전"));
                    } catch (Exception e) {
                        sseEmitters.remove(userSeq);
                    }
                }
                noticeRepository.save(new Notice(userSeq, 102, LocalDate.now(), "스터디 시작시간 10분 전입니다.", false));
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
