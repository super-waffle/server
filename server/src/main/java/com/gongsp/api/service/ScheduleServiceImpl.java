package com.gongsp.api.service;

import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudySchedule;
import com.gongsp.db.repository.StudyRepository;
import com.gongsp.db.repository.StudyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private StudyScheduleRepository studyScheduleRepository;

    @Override
    public List<StudySchedule> findAllUserIncludedActiveStudies(Integer userSeq, LocalDate date, Integer day) {
//        System.out.println(studyRepository.findAllByUserSeq(userSeq, date, day));
        return studyScheduleRepository.findAllByUserSeq(userSeq, date, day);
    }
}
