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
}
