package com.gongsp.db.repository;

import com.gongsp.db.entity.LogTime;
import com.gongsp.db.entity.MeetingOnair;
import com.gongsp.db.entity.MeetingOnairId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface LogTimeRepository extends JpaRepository<LogTime, Integer> {
    Optional<LogTime> findLogTimeByUserSeqAndLogDate(Integer userSeq, LocalDate logDate);
    boolean existsLogTimeByUserSeqAndLogDate(Integer userSeq, LocalDate logDate);
    Optional<LogTime> findTop1ByUserSeqOrderByLogDateDesc(Integer userSeq);
}
