package com.gongsp.api.response.schedule;

import com.gongsp.db.entity.StudySchedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ScheduleRes {
    private LocalDate date;
    private List<StudySchedule> studySchedules;

    public ScheduleRes(LocalDate date, List<StudySchedule> studySchedules) {
        this.date = date;
        this.studySchedules = studySchedules;
    }

    public ScheduleRes() {
    }
}
