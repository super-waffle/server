package com.gongsp.api.service;

import com.gongsp.db.entity.Achieve;
import com.gongsp.db.entity.User;
import com.gongsp.db.entity.UserAchieve;
import com.gongsp.db.repository.AchieveRepository;
import com.gongsp.db.repository.UserAchieveRepository;
import com.gongsp.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("achieveService")
public class AchieveServiceImpl implements AchieveService{

    @Autowired
    private UserAchieveRepository userAchieveRepository;

    @Autowired
    private AchieveRepository achieveRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserAchieve> getAchieveList(Integer userSeq) {
        return userAchieveRepository.findAllByUserSeq(userSeq);
    }

    @Override
    public Boolean toggleAchieveActive(Integer userSeq, Integer achieveSeq) {
        UserAchieve achievement = userAchieveRepository.findByAchieveAndUser(userSeq, achieveSeq).orElse(null);
        if (achievement == null) {
            return false;
        }
        UserAchieve existingActiveAchieve = userAchieveRepository.findActivatedAchieveByUser(userSeq).orElse(null);
        try {
            if (existingActiveAchieve != null) {
                existingActiveAchieve.setIsAchieveActive(!existingActiveAchieve.getIsAchieveActive());
                userAchieveRepository.save(existingActiveAchieve);
            }
            achievement.setIsAchieveActive(!achievement.getIsAchieveActive());
            userAchieveRepository.save(achievement);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void addAchieve(User user, Integer achieveSeq) {
        UserAchieve userAchieve = new UserAchieve();
        userAchieve.setUser(user);
        userAchieve.setAchieve(achieveRepository.findByAchieveSeq(achieveSeq));
        userAchieveRepository.save(userAchieve);
    }

    @Override
    public Boolean existingAchieve(Integer userSeq, Integer achieveSeq) {
        User user = userRepository.findUserByUserSeq(userSeq).orElse(null);
        Achieve achieve = achieveRepository.findByAchieveSeq(achieveSeq);
        return userAchieveRepository.existsUserAchieveByUserAndAchieve(user, achieve);
    }

    @Override
    public List<Integer> getAchieveSeqList(Integer userSeq) {
        return achieveRepository.findAllAchieveSeqByUserSeq(userSeq);
    }

    @Override
    public List<Achieve> getAll() {
        return achieveRepository.getAll();
    }

    @Override
    public UserAchieve getActiveAchieveSeq(Integer userSeq) {
        return userAchieveRepository.findActivatedAchieveByUser(userSeq).orElse(null);
    }
}
