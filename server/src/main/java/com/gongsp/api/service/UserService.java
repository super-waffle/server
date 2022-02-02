package com.gongsp.api.service;

import com.gongsp.db.entity.OtherUserProfile;
import com.gongsp.db.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUserSeq(Integer userSeq);
    Boolean isUserExists(Integer userSeq);
    Optional<OtherUserProfile> getOtherProfile(Integer userSeq);
}
