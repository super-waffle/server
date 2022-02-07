package com.gongsp.api.service;

import com.gongsp.db.entity.StudyMember;
import com.gongsp.db.repository.StudyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("studyMemberService")
public class StudyMemberServiceImpl implements StudyMemberService{

    @Autowired
    StudyMemberRepository studyMemberRepository;

    @Override
    public boolean existsMember(int userSeq, int studySeq) {
        return studyMemberRepository.existsStudyMemberByUserSeqAndStudySeq(userSeq, studySeq);
    }

    @Override
    public Optional<StudyMember> getStudyMember(Integer userSeq, Integer studySeq) {
        return studyMemberRepository.findStudyMemberByUserSeqAndStudySeq(userSeq, studySeq);
    }

    @Override
    public void banMember(Integer userSeq, Integer studySeq) {
        int banCnt = getStudyMember(userSeq, studySeq).get().getEjectCount();
        studyMemberRepository.save(new StudyMember(userSeq, studySeq, banCnt+1, false));
    }
}
