package com.gongsp.api.service;

import com.gongsp.db.entity.User;

public interface UserService {
    User getUserByUserSeq(Integer userSeq);
}
