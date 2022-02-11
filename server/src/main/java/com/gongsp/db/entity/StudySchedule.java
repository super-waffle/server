package com.gongsp.db.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@ToString
@Table(name = "tb_study")
public class StudySchedule {

    @Id
    private Integer studySeq;
    private Integer hostSeq;
    private Integer categorySeq;

    @Column(name = "category_name", updatable = false, insertable = false)
    private String categoryName;

    @Column(name = "study_title")
    private String title;
    @Column(name = "study_short_desc")
    private String shortDescription;
    @Column(name = "study_desc")
    private String description;
    @Column(name = "study_capacity")
    private String capacity;
    @Column(name = "study_url")
    private String url;
    @Column(name = "study_late")
    private Integer lateTime;
    @Column(name = "study_date_start")
    private LocalDate startDate;
    @Column(name = "study_date_end")
    private LocalDate endDate;
    @Column(name = "study_recruit_start")
    private LocalDate recruitStartDate;
    @Column(name = "study_recruit_end")
    private LocalDate recruitEndDate;
    @Column(name = "is_study_recruiting")
    private Boolean isRecruiting;
    @Column(name = "is_study_onair")
    private Boolean isOnAir;
    @Column(name = "time_start")
    private String StartTime;
    @Column(name = "time_end")
    private String EndTime;
    @Transient
    private Integer isAttend;

}