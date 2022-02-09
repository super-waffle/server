package com.gongsp.db.repository;

import com.gongsp.db.entity.Meeting;
import com.gongsp.db.entity.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface StudyHistoryRepository extends JpaRepository<StudyHistory, Integer> {
    boolean existsStudyHistoryByUserSeqAndStudySeqAndHistoryDate(int userSeq, int studySeq, LocalDate now);
    Optional<StudyHistory> findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(int userSeq, int studySeq, LocalDate now);
    boolean existsStudyHistoryByStudySeqAndHistoryDate(int studySeq, LocalDate curDate);
}
