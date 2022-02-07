package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@DynamicInsert
@Table(name = "tb_study")
public class StudyRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studySeq;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "hostSeq")
    private User host;

    @OneToOne
    @JoinColumn(name = "categorySeq")
    private Category category;

    @Column(name = "study_title")
    private String studyTitle;
    @Column(name = "study_short_desc")
    private String studyShortDesc;
    @Column(name = "study_desc")
    private String studyDesc;
    @Column(name = "study_capacity")
    private Integer studyCapacity;
    @Column(name = "study_url")
    private String studyUrl;
    @Column(name = "study_late")
    private Integer studyLate;
    @Column(name = "study_date_start")
    private LocalDate studyDateStart;
    @Column(name = "study_date_end")
    private LocalDate studyDateEnd;
    @Column(name = "study_recruit_start")
    private LocalDate studyRecruitStart;
    @Column(name = "study_recruit_end")
    private LocalDate studyRecruitEnd;
    @Column(name = "is_study_recruiting")
    private Boolean isStudyRecruiting;
    @Column(name = "is_study_onair")
    private Boolean isStudyOnair;
}