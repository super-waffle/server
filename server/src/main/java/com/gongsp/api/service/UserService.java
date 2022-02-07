package com.gongsp.api.service;

import com.gongsp.api.request.user.UserStudyUpdatePatchReq;
import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.db.entity.Applicant;
import com.gongsp.db.entity.OtherUserProfile;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUserSeq(Integer userSeq);
    Boolean isUserExists(Integer userSeq);
    Optional<OtherUserProfile> getOtherProfile(Integer userSeq);
    void updateUserLogTime(Integer userSeq, Integer logTime);
    boolean updateUserTimeGoal(int userSeq, int timeGoal);
    Optional<List<StudyRes>> getUserIncludedStudies(int userSeq);
    Optional<StudyRes> getUserIncludedDetailStudyInfo(int studySeq);
    Optional<Study> getStudyInfo(int studySeq);
    void patchStudyInfo(Study study, UserStudyUpdatePatchReq studyPatchInfo);
    void endStudy(Study study);
    void quitStudy(int userSeq, Study study);
    void endStudyRecruit(Study study);
    Optional<Collection<User>> getApplicantByStudySeq(int studySeq);
    void startStudy(int userSeq, Study study);
    void grantApplicant(int studySeq, int applicantSeq);
    void rejectApplicant(int studySeq, int applicantSeq);
    void kickMember(int studySeq, int kickSeq);
}
