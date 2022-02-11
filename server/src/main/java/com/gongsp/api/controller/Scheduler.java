package com.gongsp.api.controller;

import com.gongsp.api.service.LandingService;
import com.gongsp.api.service.LogTimeService;
import com.gongsp.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Scheduler {

    @Autowired
    private UserService userService;

    @Autowired
    private LogTimeService logTimeService;

    @Autowired
    private LandingService landingService;

    @Scheduled(cron = "0 0 0 * * ?")    // 매일 자정에 실행
    public void getTotalTimeAndPeople() {
        Integer people = userService.getUserCount();
        Integer totalTime = logTimeService.getTotalTime();
        landingService.saveDailyStats(people, totalTime);
    }
}
