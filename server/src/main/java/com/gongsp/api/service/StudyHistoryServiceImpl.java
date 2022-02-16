package com.gongsp.api.service;

import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.entity.StudyHistory;
import com.gongsp.db.repository.StudyDayRepository;
import com.gongsp.db.repository.StudyHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service("studyHistoryService")
public class StudyHistoryServiceImpl implements StudyHistoryService {

    @Autowired
    private StudyHistoryRepository studyHistoryRepository;
    @Autowired
    private StudyDayRepository studyDayRepository;

    @Override
    public boolean existsMemberToday(Integer userSeq, Integer studySeq, LocalDate now) {
        return studyHistoryRepository.existsStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySeq, now);
    }

    @Override
    public boolean validMemberToday(Integer userSeq, Integer studySeq, LocalDate now) {
        Optional<StudyHistory> opStudyHistory = studyHistoryRepository.findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySeq, now);
        if ((opStudyHistory.isPresent()) && opStudyHistory.get().getHistoryEject())
            return false;
        return true;
    }

    @Override
    public boolean createHistory(Integer userSeq, Integer studySeq, boolean isEjected) {
        StudyHistory studyHistory = new StudyHistory();
        studyHistory.setUserSeq(userSeq);
        studyHistory.setStudySeq(studySeq);
        studyHistory.setHistoryDate(LocalDate.now());
        Optional<StudyDay[]> opStudyDays = studyDayRepository.findAllByStudySeqOrderByDayNumber(studySeq);
        studyHistory.setHistoryLate(false);
        if (opStudyDays.isPresent()){
            Integer today = LocalDate.now().getDayOfWeek().getValue();
            StudyDay[] studyDays = opStudyDays.get();
            for (StudyDay studyday: studyDays ) {
                if(studyday.getDayNumber().equals(today)){
                    LocalTime startTime = studyday.getStartTime().plusMinutes(10);
                    System.out.println(startTime);
                    LocalTime curTime = LocalTime.now();
//                    System.out.println(curTime);
//                    System.out.println(startTime);
                    if(curTime.isAfter(startTime))
                        studyHistory.setHistoryLate(true);
                    else
                        studyHistory.setHistoryLate(false);
                    break;
                }
            }
        }
        studyHistory.setHistoryEject(false);
        studyHistory.setEjectReason(null);
        studyHistoryRepository.save(studyHistory);
        return studyHistory.getHistoryLate();
    }

    @Override
    public void updateHistoryEjected(Integer userSeq, Integer studySeq, LocalDate curDate, boolean isEjected) {
        if(!existsMemberToday(userSeq, studySeq, curDate)){
            createHistory(userSeq, studySeq, true);
        } else{
            Optional<StudyHistory> opStudyHistory = studyHistoryRepository.findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySeq, curDate);
            if ((!opStudyHistory.isPresent()) || opStudyHistory.get().getHistoryEject())
                return;
            StudyHistory studyHistory = opStudyHistory.get();
            studyHistory.setHistoryEject(true);
            studyHistoryRepository.save(studyHistory);;
        }
    }

    @Override
    public boolean isMemberLate(Integer userSeq, Integer studySeq, LocalDate curDate) {
        StudyHistory studyHistory = studyHistoryRepository.findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySeq, curDate).get();
//        if(!studyHistory.isPresent())
        return studyHistory.getHistoryLate();
    }

    @Override
    public Integer isMemberAttend(Integer userSeq, Integer studySeq, LocalDate curDate){
        Optional<StudyHistory> studyHistory = studyHistoryRepository.findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySeq, curDate);
        if(!studyHistory.isPresent())
            return 0;
        if(studyHistory.get().getHistoryLate())
            return 1;
        else
            return 2;
    }

    @Override
    public boolean existsAnyoneToday(Integer studySeq, LocalDate curDate) {
        return studyHistoryRepository.existsStudyHistoryByStudySeqAndHistoryDate(studySeq, curDate);
    }

    @Override
    public List<Object []> getHistoryList() {
        return studyHistoryRepository.historyList();
    }
}
