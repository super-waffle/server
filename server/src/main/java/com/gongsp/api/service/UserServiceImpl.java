package com.gongsp.api.service;

import com.gongsp.db.entity.User;
import com.gongsp.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByUserSeq(Integer userSeq) {
        return userRepository.findUserByUserSeq(userSeq).orElse(new User());
    }
}
