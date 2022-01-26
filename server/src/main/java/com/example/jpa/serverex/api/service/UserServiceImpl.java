package com.example.jpa.serverex.api.service;

import com.example.jpa.serverex.api.request.AccountCheckNicknamePostReq;
import com.example.jpa.serverex.api.request.AccountSignupPostReq;
import com.example.jpa.serverex.db.entity.User;
import com.example.jpa.serverex.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
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
    public User getUserByUserSeq(Long userSeq) {
        return userRepository.findUserByUserSeq(userSeq).orElse(new User());
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


