package com.gongsp.api.controller;

import com.gongsp.api.response.stat.DailyStat;
import com.gongsp.api.response.stat.Stat;
import com.gongsp.api.response.stat.UserStatGetRes;
import com.gongsp.api.service.StatService;
import com.gongsp.common.auth.GongUserDetails;
import com.gongsp.common.model.response.BaseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stats")
public class StatController {

    private int getUserSeqFromAuthentication(Authentication authentication) {
        // Token에 따른 사용자 인증 객체 내부의 사용자 정보를 가져온다
        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        // 사용자 정보 내부의 사용자 일련번호를 가져온다.
        return userDetails.getUserSeq();
    }

    @Autowired
    private StatService statService;

    @GetMapping("/year")
    public ResponseEntity<? extends BaseResponseBody> getYearStats(Authentication authentication) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        Optional<List<DailyStat>> result = statService.getUserOneYearStats(userSeq);

        if (!result.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No user Stats"));

        return ResponseEntity.ok(new UserStatGetRes<List<DailyStat>>().of(200,"Sucees", result.get()));
    }

    @GetMapping()
    public ResponseEntity<? extends BaseResponseBody> getStats(Authentication authentication, @RequestParam(value = "date")String date) {
        int userSeq = getUserSeqFromAuthentication(authentication);

        LocalDate realDate = LocalDate.parse(date);
        Optional<Stat> result = statService.getUserStat(userSeq, realDate);

        if (!result.isPresent())
            return ResponseEntity.ok(BaseResponseBody.of(404, "No user Stats"));

        return ResponseEntity.ok(new UserStatGetRes<Stat>().of(200,"Sucees", result.get()));
    }
}
