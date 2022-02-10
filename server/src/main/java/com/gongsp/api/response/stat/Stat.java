package com.gongsp.api.response.stat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class Stat {
    private LocalDate date;
    private LocalTime studyStartTime;
    private LocalTime studyEndTime;
    private Short meetingRoomTime;
    private Short studyRoomTime;
    private Integer dayTotalStudyTime;
    private Integer userTotalStudyTime;
}
