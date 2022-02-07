package com.gongsp.db.repository;

import com.gongsp.db.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
//    @Query(value = "select * from tb_apply_study join tb_user using(user_seq) where study_seq = :studySeq ;", nativeQuery = true)
    Optional<Collection<Applicant>> findAllByStudySeq(@Param(value = "studySeq") int studySeq);
}
