package com.gongsp.api.service;

import com.gongsp.db.entity.Study;
import com.gongsp.db.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private StudyRepository studyRepository;

    @Override
    public List<Study> findAllUserIncludedActiveStudies(Integer userSeq, LocalDate date, Integer day) {
        System.out.println(studyRepository.findAllByUserSeq(userSeq, date, day));
        return studyRepository.findAllByUserSeq(userSeq, date, day);
    }
}
