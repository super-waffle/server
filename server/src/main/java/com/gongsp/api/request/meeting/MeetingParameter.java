package com.gongsp.api.request.meeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingParameter {
    // 현재 페이지 번호
    private Integer page;
    // 페이지당 글 개수
    private Integer spp;
    // 페이지 시작 번호
    private Integer start;
    // 검색조건
    private Integer type;
    // 검색어
    private String key;

    public MeetingParameter() {
        this.page = 1;
        this.spp = 20;
    }
}
