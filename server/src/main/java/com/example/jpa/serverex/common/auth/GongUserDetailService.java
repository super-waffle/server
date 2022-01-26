package com.example.jpa.serverex.common.auth;



import com.example.jpa.serverex.api.service.UserService;
import com.example.jpa.serverex.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * 현재 액세스 토큰으로 부터 인증된 유저의 상세정보(활성화 여부, 만료, 롤 등) 관련 서비스 정의.
 */
@Component
public class GongUserDetailService implements UserDetailsService{
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userSeq) throws UsernameNotFoundException {
        User user = userService.getUserByUserSeq(Long.parseLong(userSeq));
        if(user != null) {
            GongUserDetails userDetails = new GongUserDetails(user);
            return userDetails;
        }
        return null;
    }
}