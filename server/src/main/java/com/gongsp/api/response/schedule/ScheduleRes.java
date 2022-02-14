package com.gongsp.api.response.schedule;

import com.gongsp.db.entity.StudySchedule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class ScheduleRes {
    private LocalDate date;
    private List<StudyDailyHistory> studySchedules;

    public ScheduleRes(LocalDate date, List<StudyDailyHistory> studySchedules) {
        this.date = date;
        this.studySchedules = studySchedules;
    }

    public ScheduleRes() {}

    public void sortSchedules() {
        Collections.sort(this.studySchedules);
    }
}
