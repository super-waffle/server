package com.gongsp.api.controller;

import com.gongsp.api.service.*;
import com.gongsp.db.entity.LogTime;
import com.gongsp.db.entity.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.aspectj.runtime.internal.Conversions.intValue;


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
    @Autowired
    private StudyHistoryService studyHistoryService;

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

    // 레벨
    @Scheduled(cron = "0 0 4 * * ?")    // 매일 4시에 실행
    public void onTheNextLevel() {
        // 어제 로그의 유저에 대하여, 해당 유저의 누적 공부시간이 각 유저의 레벨 +1에 해당하는 level_condition 이상이면 갱신
        List<LogTime> logYesterday = logTimeService.getLogByDate(LocalDate.now()).orElse(null);
        for (LogTime log: logYesterday) {
            userService.updateUserLevel(log.getUserSeq());
        }
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

    // 업적 #6 <쉬는 것도 전략>: 목표시간을 2시간 초과한 경우
    @Scheduled(cron = "0 0 7 * * ?")   // 매일 7시에 실행
    public void checkOverTwo() {
        LocalDate today = LocalDate.now();
        List<LogTime> totalTimeSpentList = logTimeService.getLogByDate(today).orElse(null);
        for (LogTime totalTimeLog: totalTimeSpentList) {
            Integer userSeq = totalTimeLog.getUserSeq();
            Integer timeGoal = userService.getUserTimeGoal(userSeq);
            if (timeGoal + 2 <= (totalTimeLog.getLogStudy() + totalTimeLog.getLogMeeting())
                    && !achieveService.existingAchieve(userSeq, 6)) {
                noticeService.sendAchieveNotice(userSeq, 6, "쉬는 것도 전략");
            }
        }
    }

    // 업적 #2 <만남은 어렵고 이별은 쉬워>: 스터디 생성 실패 (어제가 모집마감이며 시작날짜가 여전히 null 값일 때)
    @Scheduled(cron = "0 0 12 * * ?")    // 매일 정오에 실행
    public void failedToOpenStudy() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        // 스터디 시작날짜 null, 모집종료여부 true, 모집종료날짜 어제
        List<Study> recruitEndedStudyList = studyRoomService.getRecruitEndedStudyList(yesterday);
        for (Study study: recruitEndedStudyList) {
            Integer userSeq = study.getHostSeq();
            if (!achieveService.existingAchieve(userSeq, 2)) {
                noticeService.sendAchieveNotice(userSeq, 2, "만남은 어렵고 이별은 쉬워");
            }
        }
    }

    // 업적 #3 <작심삼일>: 어제, 그저께, 그끄저꼐 로그기록 연속 3개
    @Scheduled(cron = "0 0 0 * * ?")   // 매일 자정에 실행
    public void successiveThree() {
        LocalDate today = LocalDate.now();
        List<LogTime> yesterdayLogList = logTimeService.getLogByDate(today).orElse(null);
        for (LogTime log: yesterdayLogList) {
            Integer userSeq = log.getUserSeq();
            if (logTimeService.getUserLogByDate(userSeq, today.minusDays(2))
                    && logTimeService.getUserLogByDate(userSeq, today.minusDays(3))
                    && !achieveService.existingAchieve(userSeq, 3)) {
                noticeService.sendAchieveNotice(userSeq, 3, "작심삼일");
            }
        }
    }

    // 업적 #7 <지금이니?>: 스터디 정각 출석 10회 달성
    @Scheduled(cron = "0 0 4 * * ?")    // 매일 4시에 실행
    public void onTimeTen() {
        List<Object []> studyHistoryList = studyHistoryService.getHistoryList();
        for (Object [] data: studyHistoryList) {
            if (intValue(data[1]) - intValue(data[2]) == 10
                    && !achieveService.existingAchieve(intValue(data[0]), 7)) {
                noticeService.sendAchieveNotice(intValue(data[0]), 7, "지금이니?");
            }
        }
    }
}
