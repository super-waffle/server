package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountRegisterPostReq;
import com.gongsp.db.entity.User;

public interface AccountService {
    public void createUser(AccountRegisterPostReq accountRegisterPostReq) throws Exception;
    public User getUserByUserEmail(String userEmail);

}
