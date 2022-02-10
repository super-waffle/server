package com.gongsp.api.service;

import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudySchedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    List<StudySchedule> findAllUserIncludedActiveStudies(Integer userSeq, LocalDate date, Integer day);
}
