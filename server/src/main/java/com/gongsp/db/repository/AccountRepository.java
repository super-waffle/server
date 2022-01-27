package com.gongsp.db.repository;

import com.gongsp.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUserEmail(String userEmail);
    Boolean existsByUserNickname(String userNickname);
}
