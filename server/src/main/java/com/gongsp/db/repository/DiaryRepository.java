package com.gongsp.db.repository;

import com.gongsp.db.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    Optional<Diary> getDiaryByUserSeqAndDiaryDate(Integer userSeq, LocalDate date);
    Optional<Diary> getDiaryByDiarySeq(Integer diarySeq);
    Boolean existsDiaryByDiarySeq(Integer diarySeq);
}
