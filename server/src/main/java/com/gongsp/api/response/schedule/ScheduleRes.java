package com.gongsp.api.response.schedule;

import com.gongsp.db.entity.StudySchedule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ScheduleRes {
    private LocalDate date;
    private List<StudySchedule> studySchedules;

    public ScheduleRes(LocalDate date, List<StudySchedule> studySchedules) {
        this.date = date;
        this.studySchedules = studySchedules;
        System.out.println("내부" + studySchedules);
        System.out.println("STudyRES내부" + this.studySchedules.toString());
    }

    public ScheduleRes() {
    }
}
