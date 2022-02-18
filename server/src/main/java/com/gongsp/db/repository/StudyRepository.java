package com.gongsp.db.repository;

import com.gongsp.db.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Integer> {
    @Query(value = "select st.*, c.category_name\n" +
            "from tb_member_study sm left outer join tb_study st \n" +
            "on (sm.study_seq = st.study_seq)\n" +
            "inner join tb_category c\n" +
            "on (st.category_seq = c.category_seq)\n" +
            "where sm.user_seq = :seq and (st.study_date_end is null or st.study_date_end > curdate()) order by st.study_seq desc ;",
            nativeQuery = true)
    Optional<Study[]> selectAllStudies(@Param(value = "seq") int userSeq);

    @Query(value = "select st.*, c.category_name\n" +
            "from tb_member_study sm left outer join tb_study st \n" +
            "on (sm.study_seq = st.study_seq)\n" +
            "inner join tb_category c\n" +
            "on (st.category_seq = c.category_seq)\n" +
            "where sm.user_seq = :seq and (st.study_date_end is null or st.study_date_end > curdate()) " +
            "and st.study_date_start is not null order by st.study_seq desc ;",
            nativeQuery = true)
    Optional<Study[]> selectAllStudiesWithoutNotStarted(@Param("seq") int userSeq);

    @Query(value = "select *\n" +
            "from tb_study join tb_category\n" +
            "using (category_seq)\n" +
            "where study_seq = :seq ;",
            nativeQuery = true)
    Optional<Study> selectStudyDetailInfo(@Param(value = "seq") int studySeq);

    @Query(value = "SELECT st.*, c.category_name\n" +
            "FROM tb_study st\n" +
            "LEFT JOIN tb_member_study m ON st.study_seq = m.study_seq\n" +
            "LEFT JOIN tb_category c ON st.category_seq = c.category_seq\n" +
            "LEFT JOIN tb_day_study d ON st.study_seq = d.study_seq\n" +
            "WHERE m.user_seq = :userSeq AND st.study_date_start <= :date <= st.study_date_end\n" +
            "AND d.day_number = :day ;",
            nativeQuery = true)
    List<Study> findAllByUserSeq(@Param(value="userSeq") Integer userSeq, @Param(value="date") LocalDate date, @Param(value="day") Integer day);

    @Query(value = "select st.*, c.category_name\n" +
            "from tb_member_study sm left outer join tb_study st \n" +
            "on (sm.study_seq = st.study_seq)\n" +
            "inner join tb_category c\n" +
            "on (st.category_seq = c.category_seq)\n" +
            "where sm.user_seq = :userSeq\n" +
            "and (st.study_date_end is null or (st.study_date_end <= :date + interval :end day and st.study_date_end >= :date - interval :start day))\n" +
            "and st.study_date_start is not null ;",
            nativeQuery = true)
    Optional<List<Study>> selectAllStudiesBetweenGivenDates(@Param(value = "userSeq") int userSeq,
                                                            @Param(value = "date") LocalDate date,
                                                            @Param(value = "start") int startInterval,
                                                            @Param(value = "end") int endInterval);

    @Query(value = "SELECT st.*, c.category_name\n" +
            " FROM tb_study st INNER JOIN tb_category c\n" +
            "ON st.category_seq = c.category_seq\n" +
            "WHERE study_recruit_end = :yesterday AND is_study_recruiting = false AND study_date_start is null;"
            , nativeQuery = true)
    List<Study> findAllByRecruitEndDate(@Param(value="yesterday") LocalDate yesterday);
}
