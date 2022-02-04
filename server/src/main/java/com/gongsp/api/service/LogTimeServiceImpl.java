package com.gongsp.api.service;

import com.gongsp.db.entity.LogTime;
import com.gongsp.db.repository.LogTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service("logTimeService")
public class LogTimeServiceImpl implements LogTimeService {

    @Autowired
    LogTimeRepository logTimeRepository;

    @Override
    public void createLogTime(Integer userSeq) {
        LogTime logTime = new LogTime();
        logTime.setUserSeq(userSeq);
        logTime.setLogDate(LocalDate.now());
        logTime.setLogStartTime(LocalTime.now());
        logTime.setLogEndTime(LocalTime.now());
        logTimeRepository.save(logTime);
    }

    @Override
    public boolean existsLog(Integer userSeq, LocalDate date) {
        return logTimeRepository.existsLogTimeByUserSeqAndLogDate(userSeq, date);
    }

    @Override
    public void updateMeetingLogTime(Integer userSeq, Integer logMeeting, LocalTime logStart) {
        // 오늘 공부한게 있으면 update
        Optional<LogTime> opLog = logTimeRepository.findTop1ByUserSeqOrderByLogDateDesc(userSeq);
//                logTimeRepository.findLogTimeByUserSeqAndLogDate(userSeq, LocalDate.now());
        if(!opLog.isPresent()){
            System.out.println("Error : LogTime 기록이 안된 사용자");
            createLogTime(userSeq);
            return;
        }

        LogTime logTime = opLog.get();
        LocalTime curTime = LocalTime.now();
        LocalDate curDate = LocalDate.now();

        if (logTime.getLogDate().isEqual(curDate)) {
            logTime.setLogMeeting((short)(logTime.getLogMeeting() + logMeeting));
            logTime.setLogEndTime(curTime);
            logTimeRepository.save(logTime);
        } else {
            int todayMin = curTime.getHour() * 60 + curTime.getMinute();
            int firstDay = (24-logStart.getHour())*60 - logStart.getMinute() - 1;
            int beforeToday = logMeeting - todayMin - firstDay;
            LocalDate localDate = logTime.getLogDate();
            // 첫날
            logTime.setLogMeeting((short)(logTime.getLogMeeting() + firstDay));
            logTime.setLogEndTime(LocalTime.of(23, 59, 59));
            logTimeRepository.save(logTime);
            // 24시간 = 60*24 = 1440
            while(beforeToday >= 1440){
                LogTime log = new LogTime();
                log.setUserSeq(userSeq);
                log.setLogDate(localDate.plusDays(1));
                log.setLogMeeting((short)1440);
                log.setLogStartTime(LocalTime.of(0, 0, 0));
                log.setLogEndTime(LocalTime.of(23, 59, 59));
                logTimeRepository.save(log);
                beforeToday -= 1440;
            }
            //오늘
            LogTime log = new LogTime();
            log.setUserSeq(userSeq);
            log.setLogDate(curDate);
            log.setLogMeeting((short)todayMin);
            log.setLogStartTime(LocalTime.of(0, 0, 0));
            log.setLogEndTime(curTime);
            logTimeRepository.save(log);
        }
    }
}
