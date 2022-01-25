package com.gongsp.api.service;

import com.gongsp.db.entity.User;

public interface UserService {
    public User getUserByUserSeq(Integer userSeq);
}
