package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountCheckNicknamePostReq;
import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountService")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(AccountSignupPostReq signupInfo) {
        User user = new User();
        user.setUserEmail(signupInfo.getEmail());
        user.setUserNickname(signupInfo.getNickname());
        user.setUserPassword(signupInfo.getPassword());
        userRepository.save(user);
        return userRepository.findUserByUserEmail(signupInfo.getEmail()).get();
    }

    @Override
    public User getUserByUserEmail(String userEmail) {
        return userRepository.findUserByUserEmail(userEmail).orElse(null);
    }

    @Override
    public boolean getUserByUserNickname(AccountCheckNicknamePostReq nicknameInfo) {
        return userRepository.findUserByUserNickname(nicknameInfo.getNickname()).isPresent();
    }
}