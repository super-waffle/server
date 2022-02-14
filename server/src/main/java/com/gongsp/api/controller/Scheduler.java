package com.gongsp.api.controller;

import com.gongsp.api.service.LandingService;
import com.gongsp.api.service.LogTimeService;
import com.gongsp.api.service.StudyRoomService;
import com.gongsp.api.service.UserService;
import com.gongsp.db.entity.StudyRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class Scheduler {

    @Autowired
    private UserService userService;
    @Autowired
    private LogTimeService logTimeService;
    @Autowired
    private LandingService landingService;
    @Autowired
    private StudyRoomService studyRoomService;

    // 랜딩페이지 누적 인원 수 및 누적 공부시간
    @Scheduled(cron = "0 0 0 * * ?")    // 매일 자정에 실행
    public void getTotalTimeAndPeople() {
        Integer people = userService.getUserCount();
        Integer totalTime = logTimeService.getTotalTime();
        landingService.saveDailyStats(people, totalTime);
    }

    // 스터디모집 종료날짜에 마감시키기 == 스터디 모집 종료여부 true로 바꾸기
    @Scheduled(cron = "0 0 0 * * ?")    // 매일 자정에 실행
    public void endStudyRecruit() {
        studyRoomService.hideStudyRecruit(LocalDate.now().minusDays(1));
    }
}
