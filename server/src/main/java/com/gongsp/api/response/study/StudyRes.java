package com.gongsp.api.response.study;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudyRes {
    private int studySeq;
    private String hostNickname;
    private String categoryName;
    private String studyTitle;
    private String studyShortDesc;
    private int studyHeadcount;
    private LocalDate studyRecruitEnd;
}
