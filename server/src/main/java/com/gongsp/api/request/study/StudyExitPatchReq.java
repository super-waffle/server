package com.gongsp.api.request.study;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class StudyExitPatchReq {
    private String sessionToken;
    private Integer logStudy; //공부한 총 시간
    private LocalTime logStartTime;   //공부 시작시간
}

