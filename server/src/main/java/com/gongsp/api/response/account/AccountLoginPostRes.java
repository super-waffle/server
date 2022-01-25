package com.gongsp.api.response.account;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Data;

@Data
public class AccountLoginPostRes extends BaseResponseBody {
    private String accessToken;

    public static AccountLoginPostRes of(Integer statusCode, String message, String accessToken) {
        AccountLoginPostRes res = new AccountLoginPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAccessToken(accessToken);
        return res;
    }
}
