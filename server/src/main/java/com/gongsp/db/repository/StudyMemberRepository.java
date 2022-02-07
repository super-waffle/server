package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Integer> {
    @Query(value = "select *\n" +
            "from tb_user u left outer join tb_member_study sm\n" +
            "using (user_seq)\n" +
            "where study_seq = :seq",
            nativeQuery = true)
    Optional<StudyMember[]> selectAllStudyMemebers(@Param(value = "seq") int studySeq);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_member_study where user_seq = :userSeq and study_seq = :studySeq ;",
            nativeQuery = true)
    void deleteByUserSeqAndStudySeq(@Param(value = "userSeq") int userSeq, @Param(value = "studySeq") int studySeq);

    boolean existsStudyMemberByUserSeqAndStudySeq(int userSeq, int studySeq);
    int countStudyMemberByStudySeq(int userSeq);

    @Modifying
    @Transactional
    @Query(value = "insert into tb_member_study values (:applicantSeq, :studySeq, 0, false)", nativeQuery = true)
    void insertNewMember(@Param(value = "studySeq")int studySeq, @Param(value = "applicantSeq") int applicantSeq);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_member_study where study_seq = :studySeq and user_seq = :userSeq",
            nativeQuery = true)
    void kickMember(@Param(value = "studySeq")int studySeq, @Param(value = "userSeq") int userSeq);
}
