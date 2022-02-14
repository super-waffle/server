package com.gongsp.api.service;

import com.gongsp.api.request.user.UserInfoPatchReq;
import com.gongsp.api.request.user.UserMeetingPatchReq;
import com.gongsp.api.request.user.UserStudyUpdatePatchReq;
import com.gongsp.api.response.user.my_study.PagedStudyResult;
import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.db.entity.*;
import com.gongsp.db.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtherProfileRepository otherProfileRepository;

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
            Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeq(study.getStudySeq());
            studyDays.ifPresent(study::setDays);
        }

        return Optional.of(results);
    }

    @Override
    public Optional<StudyRes> getUserIncludedDetailStudyInfo(int studySeq) {
        Optional<Study> study = studyRepository.selectStudyDetailInfo(studySeq);

        if (!study.isPresent())
            return Optional.empty();

        StudyRes result = new StudyRes(study.get());

        Optional<StudyMember[]> members = studyMemberRepository.selectAllStudyMemebers(result.getStudySeq());
        members.ifPresent(result::setMemberList);

        Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeq(result.getStudySeq());
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
        storageService.delete(meetingInfo.getMeetingImg());
        String imagePath = storageService.store(meetingPatchReq.getMeetingImg());

        meetingInfo.setMeetingTitle(meetingPatchReq.getMeetingTitle());
        meetingInfo.setMeetingDesc(meetingPatchReq.getMeetingDesc());
        meetingInfo.setMeetingCamType(meetingPatchReq.getMeetingCamType());
        meetingInfo.setMeetingMicType(meetingPatchReq.getMeetingMicType());
        meetingInfo.setCategorySeq(meetingPatchReq.getCategorySeq());
        meetingInfo.setMeetingImg(imagePath);

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
    public PagedStudyResult setPagenation(List<StudyRes> studyRes, int page, int size) {
        int totalPage = studyRes.size() / size;
        if (studyRes.size() % size != 0)
            totalPage += 1;

        if (totalPage == 1)
            return new PagedStudyResult(totalPage, 1, size, studyRes);

        List<StudyRes> result = new ArrayList<>();
        int thisPage = page;

        if (page == 0)
            thisPage = 1;

        int endPage = (thisPage - 1) * size + size;

        if (endPage > studyRes.size())
            endPage = studyRes.size();

        for (int i = (thisPage - 1) * size; i < endPage; i++) {
            result.add(studyRes.get(i));
        }

        return new PagedStudyResult(totalPage, thisPage, size, result);
    }
}
