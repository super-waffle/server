package com.gongsp.api.response.stat;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatGetRes<Result> extends BaseResponseBody {
    private Result result;

    public UserStatGetRes of(int statusCode, String message, Result result) {
        this.setStatusCode(statusCode);
        this.setMessage(message);
        this.setResult(result);
        return this;
    }
}
