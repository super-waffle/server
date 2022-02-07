package com.gongsp.api.request.study;

import com.gongsp.api.response.study.Day;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudyCreatePostReq {
    private Integer hostSeq;
    private Integer categorySeq;
    private String studyTitle;
    private String studyShortDesc;
    private String studyDesc;
    private LocalDate studyRecruitEnd;
    private List<Day> day;
}
