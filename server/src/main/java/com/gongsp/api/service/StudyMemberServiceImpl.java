package com.gongsp.api.service;

import com.gongsp.db.repository.StudyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("studyMemberService")
public class StudyMemberServiceImpl implements StudyMemberService{

    @Autowired
    StudyMemberRepository studyMemberRepository;

    @Override
    public boolean existsMember(int userSeq, int studySeq) {
        return studyMemberRepository.existsStudyMemberByUserSeqAndStudySeq(userSeq, studySeq);
    }
}
