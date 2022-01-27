package com.gongsp.db.repository;

import com.gongsp.db.entity.AuthEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEmail, Integer> {
    Optional<AuthEmail> findByAuthEmail(String email);
}
