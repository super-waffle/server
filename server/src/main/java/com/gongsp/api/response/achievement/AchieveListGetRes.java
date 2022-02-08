package com.gongsp.api.response.achievement;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.UserAchieve;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AchieveListGetRes extends BaseResponseBody {
    private List<UserAchieve> achievementList;

    public static AchieveListGetRes of(Integer statusCode, String message, List<UserAchieve> achievementList) {
        AchieveListGetRes res = new AchieveListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAchievementList(achievementList);
        return res;
    }
}
