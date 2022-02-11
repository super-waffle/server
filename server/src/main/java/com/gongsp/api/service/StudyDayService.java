package com.gongsp.api.service;

import com.gongsp.api.response.study.Day;
import com.gongsp.db.entity.StudyDay;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StudyDayService {
    Optional<StudyDay[]> getStudyDay(Integer studySeq);
    void createStudyDays(List<Day> day, Integer studySeq);
    boolean isValidTime(Integer studySeq, LocalDate curDate, LocalTime curTime);
}
