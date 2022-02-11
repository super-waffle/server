package com.gongsp.db.repository;

import com.gongsp.db.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Integer> {
    @Query(value = "SELECT DISTINCT st.*, c.category_name, d.time_start, d.time_end " +
            "FROM tb_study st\n" +
            "JOIN tb_member_study m ON st.study_seq = m.study_seq\n" +
            "JOIN tb_category c ON st.category_seq = c.category_seq\n" +
            "JOIN tb_day_study d ON (st.study_seq = d.study_seq AND d.day_number = :day)" +
            "WHERE m.user_seq = :userSeq " +
            "AND study_date_start <= :date " +
            "AND study_date_end >= :date " +
            "AND d.day_number = :day ;",
            nativeQuery = true)
    List<StudySchedule> findAllByUserSeq(@Param(value = "userSeq") Integer userSeq, @Param(value = "date") LocalDate date, @Param(value = "day") Integer day);

}
