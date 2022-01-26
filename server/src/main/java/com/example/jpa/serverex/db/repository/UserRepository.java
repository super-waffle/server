package com.example.jpa.serverex.db.repository;

import com.example.jpa.serverex.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserEmail(String userEmail);
    Optional<User> findUserByUserSeq(Long userSeq);
    Optional<User> findUserByUserNickname(String userNickname);
}
