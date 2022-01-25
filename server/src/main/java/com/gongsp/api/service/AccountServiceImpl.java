package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountRegisterPostReq;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(AccountRegisterPostReq accountRegisterPostReq) throws Exception {
        User user = new User();
        user.setUserEmail(accountRegisterPostReq.getUserEmail());
        user.setUserNickname(accountRegisterPostReq.getUserNickname());
        user.setUserPassword(accountRegisterPostReq.getUserPassword());
        user.setUserPassword(accountRegisterPostReq.getUserPassword());
        user.setLevelSeq(1);
        user.setLevelImgSeq(1);
        userRepository.save(user);
        return;
    }

    @Override
    public User getUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail).orElse(null);
    }
}