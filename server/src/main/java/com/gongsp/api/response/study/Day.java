package com.gongsp.api.response.study;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class Day {
    private Integer dayNumber;
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
