package com.gongsp.api.service;

public interface EmailService {
    Boolean existsByUserEmail(String userEmail);
    Boolean sendAuthEmail(String userEmail, String authCode);
    String createAuthCode();
}
