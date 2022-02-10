package com.gongsp.api.response.stat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyStat {
    private LocalDate date;
    private Integer dayTotalTime;
}
