package com.gongsp.api.service;

import com.gongsp.db.entity.AuthEmail;

public interface EmailService {
    Boolean existsByUserEmail(String userEmail);
    Boolean sendAuthEmail(String userEmail, String authCode);
    String createAuthCode(String email);
    AuthEmail getByAuthEmail(String email);
    void deleteAuthEmail(String email);
}
