package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyApply;
import com.gongsp.db.entity.StudyApplyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyApplyRepository  extends JpaRepository<StudyApply, StudyApplyId> {
    boolean existsStudyApplyByStudyApplyId(StudyApplyId studyApplyId);
}
