package com.gongsp.api.service;

import com.gongsp.api.response.study.Day;
import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.repository.StudyDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("studyDayService")
public class StudyDayServiceImpl implements StudyDayService {

    @Autowired
    StudyDayRepository studyDayRepository;

    @Override
    public Optional<StudyDay[]> getStudyDay(Integer studySeq) {
        return studyDayRepository.findAllByStudySeq(studySeq);
    }

    @Override
    public void createStudyDays(List<Day> day, Integer studySeq) {
        StudyDay studyDay;
        for (Day d : day) {
            studyDay = new StudyDay();
            studyDay.setStudySeq(studySeq);
            studyDay.setDayNumber(d.getDayNumber());
            studyDay.setStartTime(d.getTimeStart());
            studyDay.setEndTime(d.getTimeEnd());
            studyDayRepository.save(studyDay);
        }
    }
}
