package com.gongsp.api.service;

import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.repository.StudyDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("studyDayService")
public class StudyDayServiceImpl implements StudyDayService{

    @Autowired
    StudyDayRepository studyDayRepository;

    @Override
    public Optional<StudyDay[]> getStudyDay(Integer studySeq) {
        return studyDayRepository.findAllByStudySeq(studySeq);
    }
}
