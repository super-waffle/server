package com.gongsp.db.repository;

import com.gongsp.db.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    Optional<Collection<Applicant>> findAllByStudySeq(@Param(value = "studySeq") int studySeq);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_apply_study where study_seq = :studySeq and user_seq = :userSeq", nativeQuery = true)
    void deleteApplicant(@Param(value = "studySeq") int studySeq, @Param(value = "userSeq") int userSeq);
}
