package com.gongsp.api.service;

import com.gongsp.db.entity.Study;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    List<Study> findAllUserIncludedActiveStudies(Integer userSeq, LocalDate date, Integer day);
}
