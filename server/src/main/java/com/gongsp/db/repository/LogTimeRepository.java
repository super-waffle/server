package com.gongsp.db.repository;

import com.gongsp.db.entity.LogTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Query(value = "SELECT log_end_time FROM tb_log_time\n" +
            "WHERE user_seq = :userSeq AND log_date >= :today AND log_date < :tomorrow ;",
            nativeQuery = true)
    Optional<LocalTime> findLogEndTimeByUserSeqAndDate(@Param(value="userSeq") Integer userSeq, @Param(value="today") LocalDate today, @Param(value="tomorrow") LocalDate tomorrow);

    @Query(value = "SELECT * FROM tb_log_time\n" +
            "WHERE log_date >= :yesterday AND log_date < :today ;",
            nativeQuery = true)
    Optional<List<LogTime>> getLogByDate(@Param(value="yesterday") LocalDate yesterday, @Param(value="today") LocalDate today);
}
