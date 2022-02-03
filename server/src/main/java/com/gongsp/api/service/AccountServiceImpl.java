package com.gongsp.api.service;

import com.gongsp.api.request.account.AccountSignupPostReq;
import com.gongsp.common.util.UuidUtil;
import com.gongsp.common.util.MailUtil;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UuidUtil uuidUtil;

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
    public Boolean existsByUserNickname(String userNickname) {
        return accountRepository.existsByUserNickname(userNickname);
    }

    // 임시 비밀번호 발급
    @Override
    public String createTempPassword() {
        return uuidUtil.createRandomUUID();
    }

    // 임시 비밀번호로 변경
    @Override
    public Boolean updateTempPassword(String userEmail, String tempPassword) {
        User user = accountRepository.findUserByUserEmail(userEmail).orElse(null);
        user.setUserPassword(tempPassword);
        accountRepository.save(user);
        if (user.getUserPassword().equals(tempPassword)) {
            return true;
        }
        return false;
    }
    
    // 임시 비밀번호 이메일 전송
    @Override
    public Boolean sendPasswordEmail(String userEmail, String tempPassword) {
        String mailTitle = "[공습] 임시 비밀번호 발급 안내";
        String mailContent = String.format("<div>발급된 임시 비밀번호는 <b>%s</b> 입니다.</div> <div>로그인 후, 비밀번호를 변경해주세요.</div>", tempPassword);
        return mailUtil.sendEmail(userEmail, mailTitle, mailContent);
    }


}