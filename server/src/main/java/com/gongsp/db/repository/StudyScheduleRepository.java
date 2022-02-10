package com.gongsp.db.repository;

import com.gongsp.db.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Integer> {
    @Query(value = "SELECT st.*, c.category_name, d.time_start, d.time_end " +
            "FROM tb_study st\n" +
            "LEFT JOIN tb_member_study m ON st.study_seq = m.study_seq\n" +
            "LEFT JOIN tb_category c ON st.category_seq = c.category_seq\n" +
            "LEFT JOIN tb_day_study d ON st.study_seq = d.study_seq\n" +
            "WHERE m.user_seq = :userSeq AND st.study_date_start <= :date <= st.study_date_end\n" +
            "AND d.day_number = :day ;",
            nativeQuery = true)
    List<StudySchedule> findAllByUserSeq(@Param(value = "userSeq") Integer userSeq, @Param(value = "date") LocalDate date, @Param(value = "day") Integer day);

}
