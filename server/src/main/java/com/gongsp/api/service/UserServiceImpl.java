package com.gongsp.api.service;

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

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

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
    public boolean updateUserTimeGoal(int userSeq, int timeGoal) {
        Optional<User> userInfo = userRepository.findUserByUserSeq(userSeq);
        if (userInfo.isPresent()) {
            User user = userInfo.get();
            user.setUserTimeGoal(timeGoal);
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
            Optional<StudyMember[]> members = studyMemberRepository.selectAllStudyMemebers(study.getStudySeq());
            if (members.isPresent()) {
                StudyRes temp = new StudyRes(study, members.get());
                results.add(temp);
            } else {
                StudyRes temp = new StudyRes(study);
                results.add(temp);
            }
        }

        for(StudyRes study : results) {
            Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeq(study.getStudySeq());
            if (studyDays.isPresent())
                study.setDays(studyDays.get());
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
        if (members.isPresent())
            result.setMemberList(members.get());

        Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeq(result.getStudySeq());
        if (studyDays.isPresent())
            result.setDays(studyDays.get());

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
}
