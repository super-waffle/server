package com.gongsp.api.controller;

import com.gongsp.api.response.achievement.AchieveListGetRes;
import com.gongsp.api.service.AchieveService;
import com.gongsp.db.entity.UserAchieve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchieveController {

    @Autowired
    private AchieveService achieveService;

    @GetMapping()
    public ResponseEntity<AchieveListGetRes> getAchievementList(Authentication authentication) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        List<UserAchieve> achieveList = achieveService.getAchieveList(userSeq);
        if (achieveList.size() == 0) {
            return ResponseEntity.ok(AchieveListGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(AchieveListGetRes.of(200, "Success", achieveList));
    }
}
