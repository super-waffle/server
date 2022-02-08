package com.gongsp.api.service;

import com.gongsp.db.entity.UserAchieve;

import java.util.List;

public interface AchieveService {
    List<UserAchieve> getAchieveList(Integer userSeq);
}
