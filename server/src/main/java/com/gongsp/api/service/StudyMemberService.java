package com.gongsp.api.service;

import com.gongsp.db.entity.StudyMember;

import java.util.Optional;

public interface StudyMemberService {
    boolean existsMember(int userSeq, int studySeq);
    Optional<StudyMember> getStudyMember(Integer userSeq, Integer studySeq);
    void banMember(Integer userSeq, Integer studySeq);
}
