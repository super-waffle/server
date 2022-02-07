package com.gongsp.api.service;

import com.gongsp.db.entity.StudyDay;

import java.util.List;
import java.util.Optional;

public interface StudyDayService {
    Optional<StudyDay[]> getStudyDay(Integer studySeq);
}
