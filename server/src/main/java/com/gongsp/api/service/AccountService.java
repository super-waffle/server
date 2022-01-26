package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountCheckNicknamePostReq;
import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.db.entity.User;

public interface AccountService {
    public User createUser(AccountSignupPostReq signupInfo);
    public User getUserByUserEmail(String userEmail);
    public Boolean existsByUserNickname(AccountCheckNicknamePostReq nicknameInfo);
    public Boolean updateTempPassword(String userEmail, String uuid, String tempPassword);
}
