package com.example.jpa.serverex.api.request;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Generated
public class AccountSignupPostReq {
    String email;
    String nickname;
    String password;
}
