package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Optional;

public interface StudyDayRepository extends JpaRepository<StudyDay, Integer> {
    Optional<StudyDay[]> findAllByStudySeqOrderByDayNumber(int studySeq);

    @Query(nativeQuery = true, value = "select time_start from tb_day_study " +
            "where study_seq = :studySeq and day_number = :today ;")
    LocalTime findStartTime(@Param("studySeq") int studySeq,@Param("today") int today);

    @Query(nativeQuery = true, value = "select time_end from tb_day_study " +
            "where study_seq = :studySeq and day_number = :today ;")
    LocalTime findEndTime(@Param("studySeq") int studySeq,@Param("today") int today);
}
