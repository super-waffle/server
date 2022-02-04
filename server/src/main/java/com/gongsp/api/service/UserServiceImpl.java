package com.gongsp.api.service;

import com.gongsp.db.entity.OtherUserProfile;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.OtherProfileRepository;
import com.gongsp.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtherProfileRepository otherProfileRepository;

    @Override
    public Optional<User> getUserByUserSeq(Integer userSeq) {
        return userRepository.findUserByUserSeq(userSeq);
    }

    @Override
    public Boolean isUserExists(Integer userSeq) {
        return userRepository.existsUserByUserSeq(userSeq);
    }

    @Override
    public Optional<OtherUserProfile> getOtherProfile(Integer userSeq) {
        return otherProfileRepository.selectOne(userSeq);
    }

    @Override
    public void updateUserLogTime(Integer userSeq, Integer logTime) {
        Optional<User> opUser = userRepository.findUserByUserSeq(userSeq);
        if(!opUser.isPresent()){
            System.out.println("Error : Not valid userSeq");
            return;
        }
        User user = opUser.get();
        user.setUserTimeTotal(user.getUserTimeTotal() + logTime);
        userRepository.save(user);
    }
}
