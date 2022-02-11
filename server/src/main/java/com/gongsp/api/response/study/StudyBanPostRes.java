package com.gongsp.api.response.study;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyBanPostRes extends BaseResponseBody {
    private Integer memberBanCount;

    public static StudyBanPostRes of(Integer statusCode, String message, Integer banCnt) {
        StudyBanPostRes res = new StudyBanPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setMemberBanCount(banCnt);
        return res;
    }
}
