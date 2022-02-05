package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "tb_day_study")
public class StudyDay {
    @Id
    private Integer daySeq;

    private Integer studySeq;
    private Integer dayNumber;

    @Column(name = "time_start")
    private LocalTime startTime;
    @Column(name = "time_end")
    private LocalTime endTime;
}
