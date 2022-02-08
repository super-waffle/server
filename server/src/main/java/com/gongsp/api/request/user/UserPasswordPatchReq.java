package com.gongsp.api.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordPatchReq {
    private String password;
    private String newPassword;
}
