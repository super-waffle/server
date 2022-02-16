package com.gongsp.api.response.achievement;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Achieve;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AchieveListGetRes extends BaseResponseBody {
//    private List<UserAchieve> achievementList;
    private List<Achieve> achievementList;
    private List<Integer> achieveSeqList;

    public static AchieveListGetRes of(Integer statusCode, String message, List<Achieve> achievementList, List<Integer> achieveSeqList) {
        AchieveListGetRes res = new AchieveListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAchievementList(achievementList);
        res.setAchieveSeqList(achieveSeqList);
        return res;
    }
}
