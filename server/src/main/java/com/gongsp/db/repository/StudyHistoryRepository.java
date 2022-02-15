package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyHistoryRepository extends JpaRepository<StudyHistory, Integer> {
    boolean existsStudyHistoryByUserSeqAndStudySeqAndHistoryDate(int userSeq, int studySeq, LocalDate now);
    Optional<StudyHistory> findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(int userSeq, int studySeq, LocalDate now);
    boolean existsStudyHistoryByStudySeqAndHistoryDate(int studySeq, LocalDate curDate);

    @Query(value = "SELECT user_seq, COUNT(*) as total, SUM(history_late) as late\n" +
            "FROM tb_history_study\n" +
            "GROUP BY user_seq;"
            , nativeQuery = true)
    List<Object []> historyList();
}
