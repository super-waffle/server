package com.gongsp.api.response.user;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.OtherUserProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtherUserProfileGetRes extends BaseResponseBody {

    OtherUserProfile otherUserProfileInfo;

    public static OtherUserProfileGetRes of(Integer statusCode, String message, OtherUserProfile otherUserProfileInfo) {
        OtherUserProfileGetRes result = new OtherUserProfileGetRes();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        result.setOtherUserProfileInfo(otherUserProfileInfo);

        return result;
    }
}
