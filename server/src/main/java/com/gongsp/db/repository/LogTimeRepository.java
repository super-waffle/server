package com.gongsp.db.repository;

import com.gongsp.db.entity.LogTime;
import com.gongsp.db.entity.MeetingOnair;
import com.gongsp.db.entity.MeetingOnairId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LogTimeRepository extends JpaRepository<LogTime, Integer> {
    Optional<LogTime> findLogTimeByUserSeqAndLogDate(Integer userSeq, LocalDate logDate);
    boolean existsLogTimeByUserSeqAndLogDate(Integer userSeq, LocalDate logDate);
    Optional<LogTime> findTop1ByUserSeqOrderByLogDateDesc(Integer userSeq);

    @Query(value = "select * from tb_log_time " +
            "where user_seq = :userSeq and log_date > :startDate - interval 1 year;",
            nativeQuery = true)
    Optional<List<LogTime>> selectAllOneYearTimesOfUser(@Param(value = "userSeq") int userSeq, @Param(value = "startDate") LocalDate startDate);

    @Query(value = "SELECT SUM(tb.time_total)\n" +
            "FROM (SELECT user_seq, SUM(log_meeting + log_study) AS time_total\n" +
            "FROM tb_log_time \n" +
            "GROUP BY user_seq) tb ;"
            , nativeQuery = true)
    Integer getTotalTime();
}
