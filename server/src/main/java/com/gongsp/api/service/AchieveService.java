package com.gongsp.api.service;

import com.gongsp.db.entity.Achieve;
import com.gongsp.db.entity.User;
import com.gongsp.db.entity.UserAchieve;

import java.util.List;

public interface AchieveService {
    List<UserAchieve> getAchieveList(Integer userSeq);
    Boolean toggleAchieveActive(Integer userSeq, Integer achieveSeq);
    void addAchieve(User user, Integer achieveSeq);
    Boolean existingAchieve(Integer userSeq, Integer achieveSeq);
    List<Integer> getAchieveSeqList(Integer userSeq);
    List<Achieve> getAll();
    UserAchieve getActiveAchieveSeq(Integer userSeq);
}
