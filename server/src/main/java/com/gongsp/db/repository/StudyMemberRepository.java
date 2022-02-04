package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Integer> {
    @Query(value = "select *\n" +
            "from tb_user u left outer join tb_member_study sm\n" +
            "using (user_seq)\n" +
            "where study_seq = :seq",
            nativeQuery = true)
    Optional<StudyMember[]> selectAllStudyMemebers(@Param(value = "seq") int studySeq);
}
