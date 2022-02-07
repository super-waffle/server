package com.gongsp.api.request.user;

import com.gongsp.db.entity.StudyDay;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UserStudyUpdatePatchReq {
    private int studySeq;
    private Integer categorySeq;
    private String title;
    private String shortDescription;
    private String description;
    private Integer lateTime;
    private Optional<StudyDay[]> days;
}
