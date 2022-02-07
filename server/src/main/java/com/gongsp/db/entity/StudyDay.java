package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@Table(name = "tb_day_study")
public class StudyDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer daySeq;

    private Integer studySeq;
    private Integer dayNumber;

    @Column(name = "time_start")
    private LocalTime startTime;
    @Column(name = "time_end")
    private LocalTime endTime;
}
