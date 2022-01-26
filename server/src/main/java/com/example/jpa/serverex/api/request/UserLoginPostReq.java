package com.example.jpa.serverex.api.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginPostReq {
    String email;
    String password;
}
