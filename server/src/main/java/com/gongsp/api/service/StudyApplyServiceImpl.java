package com.gongsp.api.service;

import com.gongsp.db.entity.StudyApply;
import com.gongsp.db.entity.StudyApplyId;
import com.gongsp.db.repository.StudyApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("studyApplyService")
public class StudyApplyServiceImpl implements StudyApplyService{

    @Autowired
    StudyApplyRepository studyApplyRepository;

    @Override
    public void createApplicant(StudyApplyId studyApplyId) {
        studyApplyRepository.save(new StudyApply(studyApplyId));
    }

    @Override
    public boolean existsStudyById(StudyApplyId studyApplyId) {
        return studyApplyRepository.existsStudyApplyByStudyApplyId(studyApplyId);
    }
}
