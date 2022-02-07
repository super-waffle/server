package com.gongsp.db.repository;

import com.gongsp.db.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Integer> {
    @Query(value = "select st.*, c.category_name\n" +
            "from tb_member_study sm left outer join tb_study st \n" +
            "on (sm.study_seq = st.study_seq)\n" +
            "inner join tb_category c\n" +
            "on (st.category_seq = c.category_seq)\n" +
            "where sm.user_seq = :seq and (st.study_date_end is null or st.study_date_end > curdate());",
            nativeQuery = true)
    Optional<Study[]> selectAllStudies(@Param(value = "seq") int userSeq);

    @Query(value = "select *\n" +
            "from tb_study join tb_category\n" +
            "using (category_seq)\n" +
            "where study_seq = :seq ;",
            nativeQuery = true)
    Optional<Study> selectStudyDetailInfo(@Param(value = "seq") int studySeq);
}
