package com.gongsp.api.service;

import com.gongsp.db.entity.UserAchieve;
import com.gongsp.db.repository.AchieveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("achieveService")
public class AchieveServiceImpl implements AchieveService{

    @Autowired
    private AchieveRepository achieveRepository;

    @Override
    public List<UserAchieve> getAchieveList(Integer userSeq) {
        return achieveRepository.findAllByUserSeq(userSeq);
    }

    @Override
    public Boolean toggleAchieveActive(Integer userSeq, Integer achieveSeq) {
        UserAchieve achievement = achieveRepository.findByAchieveAndUser(userSeq, achieveSeq).orElse(null);
        if (achievement == null) {
            return false;
        }
        UserAchieve existingActiveAchieve = achieveRepository.findActivatedAchieveByAchieveAndUser(userSeq, achieveSeq).orElse(null);
        try {
            if (existingActiveAchieve != null) {
                existingActiveAchieve.setIsAchieveActive(false);
                achieveRepository.save(existingActiveAchieve);
            }
            achievement.setIsAchieveActive(!achievement.getIsAchieveActive());
            achieveRepository.save(achievement);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
