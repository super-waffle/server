package com.gongsp.api.controller;

import com.gongsp.api.service.*;
import com.gongsp.db.entity.LogTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


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
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private AchieveService achieveService;

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

    // 업적 #14 <앗, 이것이 성취의 맛?>: 하루 목표시간 최초 만족 시
    @Scheduled(cron = "0 0 0 * * ?")     // 매일 자정에 실행
    public void checkStudyGoal() {
        LocalDate today = LocalDate.now();
        // 어제 날짜의 로그에서 각 사용자에게 (미팅시간 + 스터디시간) >= 목표시간이면 업적 주기
        List<LogTime> totalTimeSpentList = logTimeService.getLogByDate(today).orElse(null);
        for (LogTime totalTimeLog: totalTimeSpentList) {
            Integer userSeq = totalTimeLog.getUserSeq();
            Integer timeGoal = userService.getUserTimeGoal(userSeq);
            if (timeGoal <= (totalTimeLog.getLogStudy() + totalTimeLog.getLogMeeting())
                    && !achieveService.existingAchieve(userSeq, 14)) {
                noticeService.sendAchieveNotice(userSeq, 14, "앗, 이것이 성취의 맛?");
            }
        }
    }

    // 업적 #12 <일거양득>: 하루에 자유열람실과 스터디룸을 모두 이용했을 경우
    @Scheduled(cron = "0 0 0 * * ?")     // 매일 자정에 실행
    public void checkUseBoth() {
        LocalDate today = LocalDate.now();
        List<LogTime> logList = logTimeService.getLogByDate(today).orElse(null);
        for (LogTime log: logList) {
            Integer userSeq = log.getUserSeq();
            if (log.getLogMeeting() > 0
                    && log.getLogStudy() > 0
                    && !achieveService.existingAchieve(userSeq, 12)) {
                noticeService.sendAchieveNotice(userSeq, 12, "일거양득");
            }
        }
    }
}
