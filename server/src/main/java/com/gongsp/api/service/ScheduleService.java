package com.gongsp.api.service;

import com.gongsp.api.response.schedule.ScheduleRes;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudySchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ScheduleService {
    List<StudySchedule> findAllUserIncludedActiveStudies(Integer userSeq, LocalDate date, Integer day);
    List<ScheduleRes> getAllStudiesInAWeek(int userSeq, LocalDate date);
}
