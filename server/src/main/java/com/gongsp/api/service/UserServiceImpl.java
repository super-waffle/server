package com.gongsp.api.service;

import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.db.entity.*;
import com.gongsp.db.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<StudyRes> getUserIncludedDetailStudyInfo(int studySeq, int userSeq) {
        Optional<Study> study = studyRepository.selectMyStudyDetailInfo(studySeq, userSeq);

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
}
