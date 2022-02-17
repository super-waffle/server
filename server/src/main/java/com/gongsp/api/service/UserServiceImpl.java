package com.gongsp.api.service;

import com.gongsp.api.request.user.UserInfoPatchReq;
import com.gongsp.api.request.user.UserMeetingPatchReq;
import com.gongsp.api.request.user.UserStudyUpdatePatchReq;
import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.db.entity.*;
import com.gongsp.db.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static org.aspectj.runtime.internal.Conversions.intValue;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtherProfileRepository otherProfileRepository;
    @Autowired
    private LevelRepository levelRepository;

    // Study Related Repositories
    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private StudyMemberRepository studyMemberRepository;
    @Autowired
    private StudyDayRepository studyDayRepository;
    @Autowired
    private ApplicantRepository applicantRepository;

    // Meeting Related Repositories
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private BlacklistMeetingRepository blacklistMeetingRepository;

    @Override
    public Optional<User> getUserByUserSeq(Integer userSeq) {
        return userRepository.findUserByUserSeq(userSeq);
    }

    @Override
    public Boolean isUserExists(Integer userSeq) {
        return userRepository.existsUserByUserSeq(userSeq);
    }

    @Override
    public Optional<OtherUserProfile> getOtherProfile(Integer userSeq) {
        return otherProfileRepository.selectOne(userSeq);
    }

    @Override
    public void updateUserLogTime(Integer userSeq, Integer logTime) {
        Optional<User> opUser = userRepository.findUserByUserSeq(userSeq);
        if (!opUser.isPresent()) {
            System.out.println("Error : Not valid userSeq");
            return;
        }
        User user = opUser.get();
        user.setUserTimeTotal(user.getUserTimeTotal() + logTime);
        userRepository.save(user);
    }

    @Override
    public boolean updateUserNickname(int userSeq, String nickname) {
        Optional<User> userInfo = userRepository.findUserByUserSeq(userSeq);
        if (userInfo.isPresent()) {
            User user = userInfo.get();
            user.setUserNickname(nickname);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<StudyRes>> getUserIncludedStudies(int userSeq) {
        Optional<Study[]> myStudy = studyRepository.selectAllStudies(userSeq);

        if (!myStudy.isPresent())
            return Optional.empty();

        List<StudyRes> results = new ArrayList<>();

        Study[] studies = myStudy.get();

        for (Study study : studies) {
            StudyRes temp = new StudyRes(study);

            Optional<StudyMember[]> members = studyMemberRepository.selectAllStudyMemebers(study.getStudySeq());
            members.ifPresent(temp::setMemberList);

            results.add(temp);
        }

        for(StudyRes study : results) {
            Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeqOrderByDayNumber(study.getStudySeq());
            studyDays.ifPresent(study::setDays);
        }

        return Optional.of(results);
    }

    @Override
    public Optional<List<StudyRes>> getUserIncludedStudies(int userSeq, Integer today) {
        Optional<Study[]> myStudy = studyRepository.selectAllStudiesWithoutNotStarted(userSeq);

        if (!myStudy.isPresent())
            return Optional.empty();

        List<StudyRes> results = new ArrayList<>();

        Study[] studies = myStudy.get();

        for (Study study : studies) {
            StudyRes temp = new StudyRes(study);

            Optional<StudyMember[]> members = studyMemberRepository.selectAllStudyMemebers(study.getStudySeq());
            members.ifPresent(temp::setMemberList);

            results.add(temp);
        }

        boolean flag = false;
        List<StudyRes> realResults = new ArrayList<>();
        for(StudyRes study : results) {
            Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeqOrderByDayNumber(study.getStudySeq());
            List<StudyDay> todayStudyDays = new ArrayList<>();
            if(studyDays.isPresent()){
                for (StudyDay studyDay: studyDays.get()) {
                    if(studyDay.getDayNumber().equals(today)){
                        flag = true;
                        todayStudyDays.add(studyDay);
                    }
                }
                if(flag) {
                    study.setDays(todayStudyDays.toArray(new StudyDay[todayStudyDays.size()]));
                    flag = false;
                    todayStudyDays.clear();
                    realResults.add(study);
                }
            }
        }

        return Optional.of(realResults);
    }



    @Override
    public Optional<StudyRes> getUserIncludedDetailStudyInfo(int studySeq) {
        Optional<Study> study = studyRepository.selectStudyDetailInfo(studySeq);

        if (!study.isPresent())
            return Optional.empty();

        StudyRes result = new StudyRes(study.get());

        Optional<StudyMember[]> members = studyMemberRepository.selectAllStudyMemebers(result.getStudySeq());
        members.ifPresent(result::setMemberList);

        Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeqOrderByDayNumber(result.getStudySeq());
        studyDays.ifPresent(result::setDays);

        return Optional.of(result);
    }

    @Override
    public Optional<Study> getStudyInfo(int studySeq) {
        return studyRepository.selectStudyDetailInfo(studySeq);
    }

    @Override
    @Transactional
    public void patchStudyInfo(Study study, UserStudyUpdatePatchReq studyPatchInfo) {
        study.setCategorySeq(studyPatchInfo.getCategorySeq());
        study.setTitle(studyPatchInfo.getTitle());
        study.setShortDescription(studyPatchInfo.getShortDescription());
        study.setDescription(studyPatchInfo.getDescription());
        study.setLateTime(studyPatchInfo.getLateTime());
        studyRepository.save(study);

        if (studyPatchInfo.getDays().isPresent()) {
            StudyDay[] days = studyPatchInfo.getDays().get();

            for (StudyDay day : days) {
                if (day.getDaySeq() != -1)
                    studyDayRepository.save(day);
                else {
                    day.setDaySeq(0);
                    studyDayRepository.save(day);
                }
            }
        }
    }

    @Override
    public void endStudy(Study study) {
        study.setEndDate(LocalDate.now());
        studyRepository.save(study);
    }

    @Override
    public void quitStudy(int userSeq, Study study) {
        studyMemberRepository.deleteByUserSeqAndStudySeq(userSeq, study.getStudySeq());
    }

    @Override
    public void endStudyRecruit(Study study) {
        study.setRecruitEndDate(LocalDate.now());
        study.setIsRecruiting(false);
        studyRepository.save(study);
    }

    @Override
    public Optional<Collection<User>> getApplicantByStudySeq(int studySeq) {
        Optional<Collection<Applicant>> applicants = applicantRepository.findAllByStudySeq(studySeq);
        if (!applicants.isPresent())
            return Optional.empty();
        Collection<User> users = new ArrayList<>();

        for (Applicant applicant : applicants.get()) {
            users.add(applicant.getApplicant());
        }

        return Optional.of(users);
    }

    @Override
    public void startStudy(int userSeq, Study study) {
        StringBuilder sessionURLBuilder = new StringBuilder();
        sessionURLBuilder.append("Study").append(study.getStudySeq()).append("hst").append(userSeq).append("linkURL");
        study.setUrl(sessionURLBuilder.toString());
        study.setStartDate(LocalDate.now());
        studyRepository.save(study);
    }

    @Override
    @Transactional
    public void grantApplicant(int studySeq, int applicantSeq) {
        studyMemberRepository.insertNewMember(studySeq, applicantSeq);
        applicantRepository.deleteApplicant(studySeq, applicantSeq);
    }

    @Override
    public void rejectApplicant(int studySeq, int applicantSeq) {
        applicantRepository.deleteApplicant(studySeq, applicantSeq);
    }

    @Override
    public void kickMember(int studySeq, int kickSeq) {
        studyMemberRepository.kickMember(studySeq, kickSeq);
    }

    @Override
    public Optional<Meeting> getMyMeetingRoomInfo(int userSeq) {
        return meetingRepository.findTopByHostSeq(userSeq);
    }

    @Override
    public void updateMeetingInfo(Meeting meetingInfo, UserMeetingPatchReq meetingPatchReq) {
        if (meetingPatchReq.getMeetingImg() != null) {
            storageService.delete(meetingInfo.getMeetingImg());
            String imagePath = storageService.store(meetingPatchReq.getMeetingImg());
            meetingInfo.setMeetingImg(imagePath);
        }

        meetingInfo.setMeetingTitle(meetingPatchReq.getMeetingTitle());
        meetingInfo.setMeetingDesc(meetingPatchReq.getMeetingDesc());
        meetingInfo.setMeetingCamType(meetingPatchReq.getMeetingCamType());
        meetingInfo.setMeetingMicType(meetingPatchReq.getMeetingMicType());
        meetingInfo.setCategorySeq(meetingPatchReq.getCategorySeq());

        meetingRepository.save(meetingInfo);
    }

    @Override
    @Transactional
    public void deleteMeeting(Meeting meetingInfo) {
        blacklistMeetingRepository.deleteAllByMeetingSeq(meetingInfo.getMeetingSeq());
        bookmarkRepository.deleteAllByMeetingSeq(meetingInfo.getMeetingSeq());
        meetingRepository.delete(meetingInfo);
    }

    @Override
    public void updateUserPassword(User user, String newPassword) {
        user.setUserPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public void updateUserProfile(User user, UserInfoPatchReq infoPatchReq) {
        if (infoPatchReq.getTimeGoal() != null)
            user.setUserTimeGoal(infoPatchReq.getTimeGoal());
        if (infoPatchReq.getProfileMessage() != null)
            user.setUserProfileMsg(infoPatchReq.getProfileMessage());
        if (infoPatchReq.getProfileImage() != null) {
            String path = user.getUserImg();
            if (path != null)
                storageService.delete(path);
            String newPath = storageService.store(infoPatchReq.getProfileImage());
            user.setUserImg(newPath);
        }
        userRepository.save(user);
    }

    @Override
    public Integer getUserCount() {
        return userRepository.getUserCount();
    }
      
    @Override
    public void deleteProfileImage(User user) {
        user.setUserImg(null);
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUserNickname(String userNickname) {
        return userRepository.findUserByUserNickname(userNickname);
    }

    @Override
    public Integer getUserTimeGoal(Integer userSeq) {
        return userRepository.getUserTimeGoal(userSeq);
    }

    @Override
    public void updateUserLevel(Integer userSeq) {
        User user = userRepository.findUserByUserSeq(userSeq).orElse(null);
        if (user != null) {
            List<Object[]> userLevelStatusList = userRepository.getNextLevelConditionByUserSeq(userSeq);
            for (Object[] data: userLevelStatusList) {
                if (intValue(data[1]) >= intValue(data[2])) {
                    try {
                        Integer newLevel = user.getUserLevel().getLevelSeq() + 1;
                        user.setUserLevel(levelRepository.getLevelByLevelSeq(newLevel));
                        user.setUserImageLevel(levelRepository.getLevelByLevelSeq(newLevel));
                        userRepository.save(user);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

    @Override
    public String getUserNickname(Integer userSeq) {
        return userRepository.findUserNicknameByUserSeq(userSeq);
    }
}
