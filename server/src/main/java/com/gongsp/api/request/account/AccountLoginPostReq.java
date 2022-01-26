package com.gongsp.api.request.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountLoginPostReq {
    private String email;
    private String password;
}