package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@DynamicInsert
@ToString
@Table(name="tb_history_study")
public class StudyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer historySeq;
    Integer userSeq;
    Integer studySeq;
    LocalDate historyDate;
    Boolean historyLate;
    Boolean historyEject;
    String ejectReason;
}
