package com.gongsp.api.service;

import com.gongsp.db.entity.User;
import com.gongsp.db.entity.UserAchieve;
import com.gongsp.db.repository.AchieveRepository;
import com.gongsp.db.repository.UserAchieveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("achieveService")
public class AchieveServiceImpl implements AchieveService{

    @Autowired
    private UserAchieveRepository userAchieveRepository;

    @Autowired
    private AchieveRepository achieveRepository;

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
        UserAchieve existingActiveAchieve = userAchieveRepository.findActivatedAchieveByAchieveAndUser(userSeq, achieveSeq).orElse(null);
        try {
            if (existingActiveAchieve != null) {
                existingActiveAchieve.setIsAchieveActive(false);
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
}
