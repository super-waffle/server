package com.gongsp.api.request.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyCodePostReq {
    String email;
    String authCode;
}
