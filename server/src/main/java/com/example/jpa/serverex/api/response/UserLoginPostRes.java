package com.example.jpa.serverex.api.response;


import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jpa.serverex.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 로그인 API ([POST] /accounts/login) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
public class UserLoginPostRes extends BaseResponseBody {
    String accessToken;
    //DecodedJWT decodedJWT;

    public static UserLoginPostRes of(Integer statusCode, String message, String accessToken) {
        UserLoginPostRes res = new UserLoginPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAccessToken(accessToken);
        return res;
    }
}
