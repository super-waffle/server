package com.gongsp.api.response.user;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileGetRes extends BaseResponseBody {
    private User user;

    public static UserProfileGetRes of(Integer statusCode, String message, User userInfo) {
        UserProfileGetRes result = new UserProfileGetRes();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        result.setUser(userInfo);

        return result;
    }
}
