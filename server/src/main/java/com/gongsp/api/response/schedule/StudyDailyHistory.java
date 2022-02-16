package com.gongsp.api.response.schedule;

import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudyDay;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class StudyDailyHistory implements Comparable<StudyDailyHistory> {

    private Integer studySeq;
    private Integer hostSeq;
    private Integer categorySeq;
    private String categoryName;
    private String title;
    private String shortDescription;
    private String description;
    private String capacity;
    private String url;
    private Integer lateTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;
    private Boolean isRecruiting;
    private Boolean isOnAir;
    private LocalTime startTime;
    private LocalTime endTime;
    private Short isAttend;

    public StudyDailyHistory() {}

    public StudyDailyHistory(Study study, StudyDay studyDay) {
        this.setWithStudy(study);
        this.setWithDay(studyDay);
    }

    private void setWithDay(StudyDay studyDay) {
        this.startTime = studyDay.getStartTime();
        this.endTime = studyDay.getEndTime();
    }

    public void setWithStudy(Study study) {
        this.studySeq = study.getStudySeq();
        this.hostSeq = study.getHostSeq();
        this.categorySeq = study.getCategorySeq();
        this.categoryName = study.getCategoryName();
        this.title = study.getTitle();
        this.shortDescription = study.getShortDescription();
        this.description = study.getDescription();
        this.capacity = study.getCapacity();
        this.url = study.getUrl();
        this.lateTime = study.getLateTime();
        this.startDate = study.getStartDate();
        this.endDate = study.getEndDate();
        this.recruitEndDate = study.getRecruitEndDate();
        this.recruitStartDate = study.getRecruitStartDate();
        this.isRecruiting = study.getIsRecruiting();
        this.isOnAir = study.getIsOnAir();
    }

    @Override
    public int compareTo(StudyDailyHistory o) {
        return this.startTime.compareTo(o.startTime);
    }
}
