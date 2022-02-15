package com.gongsp.api.service;

import com.gongsp.db.entity.LogTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface LogTimeService {
    void createLogTime(Integer userSeq);
    boolean existsLog(Integer userSeq, LocalDate date);
    void updateMeetingLogTime(Integer userSeq, Integer logMeeting, LocalTime logStart);
    void updateStudyLogTime(Integer userSeq, Integer logStudy, LocalTime logStartTime);
    Integer getTotalTime();
    LocalTime getEndTime(Integer userSeq, LocalDate today);
    Optional<List<LogTime>> getLogByDate(LocalDate today);
    Boolean getUserLogByDate(Integer userSeq, LocalDate date);
}
