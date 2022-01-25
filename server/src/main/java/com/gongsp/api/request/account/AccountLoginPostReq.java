package com.gongsp.api.request.account;

import lombok.Data;

@Data
public class AccountLoginPostReq {
    private String userEmail;
    private String userPassword;
}