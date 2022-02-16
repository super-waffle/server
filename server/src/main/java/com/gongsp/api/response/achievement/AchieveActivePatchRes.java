package com.gongsp.api.response.achievement;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchieveActivePatchRes extends BaseResponseBody {
    private Integer activeAchieveSeq;

    public static AchieveActivePatchRes of(Integer statusCode, String message, Integer activeAchieveSeq) {
        AchieveActivePatchRes res = new AchieveActivePatchRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setActiveAchieveSeq(activeAchieveSeq);
        return res;
    }
}
