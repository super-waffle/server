package com.gongsp.api.controller;

import com.gongsp.api.response.schedule.ScheduleListGetRes;
import com.gongsp.api.response.schedule.ScheduleRes;
import com.gongsp.api.response.study.StudyRes;
import com.gongsp.api.service.ScheduleService;
import com.gongsp.db.entity.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping()
    public ResponseEntity<ScheduleListGetRes> getScheduleList(Authentication authentication, @RequestParam("date") String date) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        LocalDate todayDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Integer dayOfWeek = todayDate.getDayOfWeek().getValue();    // 1: 월, 7: 일

        Map<Integer, ScheduleRes> map = new HashMap<>();
        for (LocalDate currentDate = todayDate.minusDays(dayOfWeek-1); currentDate.isBefore(todayDate.plusDays(8-dayOfWeek)); currentDate=currentDate.plusDays(1)) {
            Integer dayNumber = currentDate.getDayOfWeek().getValue();
            map.put(dayNumber, new ScheduleRes(currentDate, scheduleService.findAllUserIncludedActiveStudies(userSeq, currentDate, dayNumber)));
        }
        return ResponseEntity.ok(ScheduleListGetRes.of(200, "Success", map));

    }
}
