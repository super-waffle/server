package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountCheckNicknamePostReq;
import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountService")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public User createUser(AccountSignupPostReq signupInfo) {
        User user = new User();
        user.setUserEmail(signupInfo.getEmail());
        user.setUserNickname(signupInfo.getNickname());
        user.setUserPassword(signupInfo.getPassword());
        accountRepository.save(user);
        return accountRepository.findUserByUserEmail(signupInfo.getEmail()).get();
    }

    @Override
    public User getUserByUserEmail(String userEmail) {
        return accountRepository.findUserByUserEmail(userEmail).orElse(null);
    }

    @Override
    public Boolean existsByUserNickname(AccountCheckNicknamePostReq nicknameInfo) {
        return accountRepository.existsByUserNickname(nicknameInfo.getNickname());
    }

    @Override
    public Boolean updateTempPassword(String userEmail, String tempPassword) {
        User user = accountRepository.findUserByUserEmail(userEmail).orElse(null);
        user.setUserPassword(tempPassword);
        accountRepository.save(user);
        return null;
    }
}