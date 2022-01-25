package com.gongsp.api.request.account;

import lombok.Data;

@Data
public class AccountRegisterPostReq {
    private String userEmail;
    private String userNickname;
    private String userPassword;
}
