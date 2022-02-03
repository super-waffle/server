package com.gongsp.api.request.meeting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class MeetingExitDeleteReq {
    private String sessionToken;
    private Integer logMeeting; //공부한 총 시간
//    private LocalDate logDate;  //공부를 시작한 날짜
    private LocalTime logStartTime;   //공부 시작시간
}

