package com.gongsp.db.repository;

import com.gongsp.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<User, Integer> {
    Boolean existsByUserEmail(String userEmail);
}
