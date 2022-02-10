package com.gongsp.api.service;

import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudyHistory;
import com.gongsp.db.entity.StudySchedule;
import com.gongsp.db.repository.StudyHistoryRepository;
import com.gongsp.db.repository.StudyRepository;
import com.gongsp.db.repository.StudyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private StudyScheduleRepository studyScheduleRepository;
    @Autowired
    private StudyHistoryRepository studyHistoryRepository;

    @Override
    public List<StudySchedule> findAllUserIncludedActiveStudies(Integer userSeq, LocalDate date, Integer day) {
//        System.out.println(studyRepository.findAllByUserSeq(userSeq, date, day));
        List<StudySchedule> studySchedules = studyScheduleRepository.findAllByUserSeq(userSeq, date, day);
        if(studySchedules == null)
            return null;
        for (int i = 0; i < studySchedules.size(); i++) {
            Optional<StudyHistory> optionalStudyHistory= studyHistoryRepository.findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySchedules.get(i).getStudySeq(), date);
            if(!optionalStudyHistory.isPresent())
                studySchedules.get(i).setIsAttend(2);
            else{
                if(optionalStudyHistory.get().getHistoryLate()) {
                    System.out.println(optionalStudyHistory.get().getHistoryLate());
                    studySchedules.get(i).setIsAttend(1);
                    System.out.println("출석!!" + studySchedules.get(i).getIsAttend());
                }
                else
                    studySchedules.get(i).setIsAttend(0);
            }
        }
        return studySchedules;
    }
}
