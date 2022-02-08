package com.gongsp.api.service;

import com.gongsp.db.entity.StudyRoomMember;

import java.util.Optional;

public interface StudyRoomMemberService {
    boolean existsMember(int userSeq, int studySeq);
    Optional<StudyRoomMember> getStudyMember(Integer userSeq, Integer studySeq);
    void banMember(Integer userSeq, Integer studySeq);
    void updateMemberOnair(Integer userSeq, Integer studySeq, boolean isOnair);
    int getStudyOnairCnt(Integer studySeq);
    void createMember(Integer userSeq, Integer studySeq);
}
