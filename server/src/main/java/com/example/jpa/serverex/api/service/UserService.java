package com.example.jpa.serverex.api.service;

import com.example.jpa.serverex.api.request.AccountCheckNicknamePostReq;
import com.example.jpa.serverex.api.request.AccountSignupPostReq;
import com.example.jpa.serverex.db.entity.User;

public interface UserService {
    User createUser(AccountSignupPostReq signupInfo);
    User getUserByUserSeq(Long userSeq);
    User getUserByUserEmail(String userEmail);
    boolean getUserByUserNickname(AccountCheckNicknamePostReq nicknameInfo);
}