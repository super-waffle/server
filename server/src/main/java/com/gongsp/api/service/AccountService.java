package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.db.entity.User;

public interface AccountService {
    User createUser(AccountSignupPostReq signupInfo);
    User getUserByUserEmail(String userEmail);
    Boolean existsByUserNickname(String userNickname);
    String createTempPassword();
    Boolean updateTempPassword(String userEmail, String tempPassword);
    Boolean sendPasswordEmail(String userEmail, String tempPassword);
}
