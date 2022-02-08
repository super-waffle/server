package com.gongsp.api.controller;

import com.gongsp.api.response.achievement.AchieveListGetRes;
import com.gongsp.api.service.AchieveService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.UserAchieve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchieveController {

    @Autowired
    private AchieveService achieveService;

    // 업적목록 조회
    @GetMapping()
    public ResponseEntity<AchieveListGetRes> getAchievementList(Authentication authentication) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        List<UserAchieve> achieveList = achieveService.getAchieveList(userSeq);
        if (achieveList.size() == 0) {
            return ResponseEntity.ok(AchieveListGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(AchieveListGetRes.of(200, "Success", achieveList));
    }

    // 대표 업적 등록/해제
    @PatchMapping("/{achieve_seq}")
    public ResponseEntity<? extends BaseResponseBody> toggleActive(Authentication authentication,
                                                                      @PathVariable("achieve_seq") Integer achieveSeq) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        Boolean toggled = achieveService.toggleAchieveActive(userSeq, achieveSeq);
        if (toggled) {
            return ResponseEntity.ok(BaseResponseBody.of(201, "Achievement Activation toggled"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(409, "Failed to toggle activation"));
    }
}
