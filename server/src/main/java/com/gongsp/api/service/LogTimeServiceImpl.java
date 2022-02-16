package com.gongsp.api.service;

import com.gongsp.db.entity.LogTime;
import com.gongsp.db.repository.LogTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
            opLog = logTimeRepository.findTop1ByUserSeqOrderByLogDateDesc(userSeq);
        }

        LogTime logTime = opLog.get();
        LocalTime curTime = LocalTime.now();
        LocalDate curDate = LocalDate.now();

        if (logTime.getLogDate().isEqual(curDate)) {
            logTime.setLogMeeting((short)(logTime.getLogMeeting() + logMeeting));
            logTime.setLogEndTime(curTime);
            logTimeRepository.save(logTime);
        } else {
            // 공부기록이 오늘보다 작으면 오늘에 넣고
            // 공부기록이 오늘보다 크면 오늘에 넣고 남은걸 어제로 넣음 -> 더 남으면 더 이전날로
            int todayMin = curTime.getHour() * 60 + curTime.getMinute();
            LogTime log = new LogTime();
            log.setUserSeq(userSeq);
            log.setLogDate(curDate);
            log.setLogStartTime(LocalTime.of(0, 0, 0));
            log.setLogEndTime(curTime);
            if(todayMin > logMeeting){
                log.setLogMeeting(logMeeting.shortValue());
                logTimeRepository.save(log);
            }else{
                log.setLogMeeting((short)todayMin);
                logTimeRepository.save(log);

                logMeeting -= todayMin;
                while(logMeeting >= 1440){ //하루 넘어갔음
                    log = new LogTime();
                    log.setUserSeq(userSeq);
                    log.setLogDate(curDate.plusDays(-1));
                    log.setLogMeeting((short)1440);
                    log.setLogStartTime(LocalTime.of(0, 0, 0));
                    log.setLogEndTime(LocalTime.of(23, 59, 59));
                    logTimeRepository.save(log);
                    logMeeting -= 1440;
                }
                log = new LogTime();
                log.setUserSeq(userSeq);
                log.setLogDate(curDate.plusDays(-1));
                log.setLogMeeting(logMeeting.shortValue());
                log.setLogStartTime(logStart);
                log.setLogEndTime(LocalTime.of(23, 59, 59));
                logTimeRepository.save(log);
            }
        }
    }

    @Override
    public void updateStudyLogTime(Integer userSeq, Integer logStudy, LocalTime logStart) {
        // 오늘 공부한게 있으면 update
        Optional<LogTime> opLog = logTimeRepository.findTop1ByUserSeqOrderByLogDateDesc(userSeq);
        if(!opLog.isPresent()){
            System.out.println("Error : LogTime 기록이 안된 사용자");
            createLogTime(userSeq);
            opLog = logTimeRepository.findTop1ByUserSeqOrderByLogDateDesc(userSeq);
        }

        LogTime logTime = opLog.get();
        LocalTime curTime = LocalTime.now();
        LocalDate curDate = LocalDate.now();

        if (logTime.getLogDate().isEqual(curDate)) {
            logTime.setLogStudy((short)(logTime.getLogStudy() + logStudy));
            logTime.setLogEndTime(curTime);
            logTimeRepository.save(logTime);
        } else {
            // 공부기록이 오늘보다 작으면 오늘에 넣고
            // 공부기록이 오늘보다 크면 오늘에 넣고 남은걸 어제로 넣음 -> 더 남으면 더 이전날로
            int todayMin = curTime.getHour() * 60 + curTime.getMinute();
            LogTime log = new LogTime();
            log.setUserSeq(userSeq);
            log.setLogDate(curDate);
            log.setLogStartTime(LocalTime.of(0, 0, 0));
            log.setLogEndTime(curTime);
            if(todayMin > logStudy){
                log.setLogStudy(logStudy.shortValue());
                logTimeRepository.save(log);
            }else{
                log.setLogStudy((short)todayMin);
                logTimeRepository.save(log);

                logStudy -= todayMin;
                while(logStudy >= 1440){ //하루 넘어갔음. 스터디는 이런일 없을거같긴한데 혹시모르니..
                    log = new LogTime();
                    log.setUserSeq(userSeq);
                    log.setLogDate(curDate.plusDays(-1));
                    log.setLogStudy((short)1440);
                    log.setLogStartTime(LocalTime.of(0, 0, 0));
                    log.setLogEndTime(LocalTime.of(23, 59, 59));
                    logTimeRepository.save(log);
                    logStudy -= 1440;
                }
                log = new LogTime();
                log.setUserSeq(userSeq);
                log.setLogDate(curDate.plusDays(-1));
                log.setLogStudy(logStudy.shortValue());
                log.setLogStartTime(logStart);
                log.setLogEndTime(LocalTime.of(23, 59, 59));
                logTimeRepository.save(log);
            }
        }
    }

    @Override
    public Integer getTotalTime() {
        return logTimeRepository.getTotalTime();
    }

    @Override
    public LocalTime getEndTime(Integer userSeq, LocalDate today) {
        return logTimeRepository.findLogEndTimeByUserSeqAndDate(userSeq, today, today.plusDays(1)).orElse(null);
    }

    @Override
    public Optional<List<LogTime>> getLogByDate(LocalDate today) {
        return logTimeRepository.getLogByDate(today.minusDays(1), today);
    }

    @Override
    public Boolean getUserLogByDate(Integer userSeq, LocalDate date) {
        LocalTime logEndTime = logTimeRepository.findLogEndTimeByUserSeqAndDate(userSeq, date, date.plusDays(1)).orElse(null);
        if (logEndTime == null) {
            return false;
        }
        return true;
    }
}
