package com.gongsp.api.response.achievement;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.UserAchieve;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchieveActiveGetRes extends BaseResponseBody {
    private UserAchieve achieveActive;

    public static AchieveActiveGetRes of(Integer statusCode, String message, UserAchieve achieveActive) {
        AchieveActiveGetRes res = new AchieveActiveGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAchieveActive(achieveActive);
        return res;
    }
}
