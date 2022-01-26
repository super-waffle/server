package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountCheckNicknamePostReq;
import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.db.entity.User;

public interface AccountService {
    User createUser(AccountSignupPostReq signupInfo);
    User getUserByUserEmail(String userEmail);
    Boolean existsByUserNickname(AccountCheckNicknamePostReq nicknameInfo);
}
