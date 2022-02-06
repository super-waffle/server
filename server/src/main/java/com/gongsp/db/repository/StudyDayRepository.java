package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyDayRepository extends JpaRepository<StudyDay, Integer> {
    Optional<StudyDay[]> findAllByStudySeq(int studySeq);
}
