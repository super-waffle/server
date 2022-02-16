package com.gongsp.api.service;

import com.gongsp.api.request.user.UserInfoPatchReq;
import com.gongsp.api.request.user.UserMeetingPatchReq;
import com.gongsp.api.request.user.UserStudyUpdatePatchReq;
import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.db.entity.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUserSeq(Integer userSeq);
    Boolean isUserExists(Integer userSeq);
    Optional<OtherUserProfile> getOtherProfile(Integer userSeq);
    void updateUserLogTime(Integer userSeq, Integer logTime);
    boolean updateUserNickname(int userSeq, String nickname);
    Optional<List<StudyRes>> getUserIncludedStudies(int userSeq);
    Optional<List<StudyRes>> getUserIncludedStudies(int userSeq, Integer today);
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
    Optional<Meeting> getMyMeetingRoomInfo(int userSeq);
    void updateMeetingInfo(Meeting meetingInfo, UserMeetingPatchReq meetingPatchReq);
    void deleteMeeting(Meeting meetingInfo);
    void updateUserPassword(User user, String newPassword);
    void updateUserProfile(User user, UserInfoPatchReq infoPatchReq);
    Integer getUserCount();
    void deleteProfileImage(User userSeq);
    Optional<User> getUserByUserNickname(String userNickname);
    Integer getUserTimeGoal(Integer userSeq);
    void updateUserLevel(Integer userSeq);
    String getUserNickname(Integer userSeq);
}
