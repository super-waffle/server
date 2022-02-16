package com.gongsp.api.controller;

import com.gongsp.api.response.schedule.ScheduleListGetRes;
import com.gongsp.api.response.schedule.ScheduleRes;
import com.gongsp.api.service.ScheduleService;
import com.gongsp.db.entity.StudySchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping()
    public ResponseEntity<ScheduleListGetRes> getScheduleList(Authentication authentication, @RequestParam("date") String date) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());

        LocalDate realDate = LocalDate.parse(date);

        List<ScheduleRes> scheduleResInfo = scheduleService.getAllStudiesInAWeek(userSeq, realDate);

        return ResponseEntity.ok(ScheduleListGetRes.of(200, "Success", scheduleResInfo));
    }
}
