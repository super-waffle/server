package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@Table(name="tb_log_time")
public class LogTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logSeq;
    private Integer userSeq;
    private Short logMeeting;
    private Short logStudy;
    private LocalDate logDate;
    private LocalTime logStartTime;
    private LocalTime logEndTime;
}
