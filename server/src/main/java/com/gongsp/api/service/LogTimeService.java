package com.gongsp.api.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface LogTimeService {
    void createLogTime(Integer userSeq);
    boolean existsLog(Integer userSeq, LocalDate date);
    void updateMeetingLogTime(Integer userSeq, Integer logMeeting, LocalTime logStart);
}
