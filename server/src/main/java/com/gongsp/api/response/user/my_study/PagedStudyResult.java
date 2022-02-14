package com.gongsp.api.response.user.my_study;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedStudyResult {
    private int totalPage;
    private int currentPage;
    private int size;
    private List<StudyRes> studyList;

    public PagedStudyResult() {}

    public PagedStudyResult(int totalPage, int currentPage, int size, List<StudyRes> studyList) {
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.size = size;
        this.studyList = studyList;
    }
}
