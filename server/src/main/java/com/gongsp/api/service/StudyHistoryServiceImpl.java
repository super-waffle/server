package com.gongsp.api.service;

import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.entity.StudyHistory;
import com.gongsp.db.repository.StudyDayRepository;
import com.gongsp.db.repository.StudyHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service("studyHistoryService")
public class StudyHistoryServiceImpl implements StudyHistoryService {

    @Autowired
    StudyHistoryRepository studyHistoryRepository;
    @Autowired
    StudyDayRepository studyDayRepository;

    @Override
    public boolean existsMemberToday(Integer userSeq, Integer studySeq, LocalDate now) {
        return studyHistoryRepository.existsStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySeq, now);
    }

    @Override
    public boolean validMemberToday(Integer userSeq, Integer studySeq, LocalDate now) {
        Optional<StudyHistory> opStudyHistory = studyHistoryRepository.findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySeq, now);
        if ((!opStudyHistory.isPresent()) || opStudyHistory.get().getHistoryEject())
            return false;
        return true;
    }

    @Override
    public void createHistory(Integer userSeq, Integer studySeq) {
        StudyHistory studyHistory = new StudyHistory();
        studyHistory.setUserSeq(userSeq);
        studyHistory.setStudySeq(studySeq);
        studyHistory.setHistoryDate(LocalDate.now());
        Optional<StudyDay[]> opStudyDays = studyDayRepository.findAllByStudySeq(studySeq);
        if (!opStudyDays.isPresent())
            studyHistory.setHistoryLate(false);
        else {
            StudyDay[] studyDays = opStudyDays.get();
//            studyDays[0].
        }
//        studyHistory.

//        Integer historySeq;
//        Integer userSeq;
//        Integer studySeq;
//        LocalDate historyDate;
//        Boolean historyLate;
//        Boolean historyEject;
//        String ejectReason;
    }
}
