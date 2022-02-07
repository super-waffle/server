package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class StudyApplyId implements Serializable {
    Integer studySeq;
    Integer userSeq;

    public StudyApplyId(Integer userSeq, Integer studySeq) {
        this.studySeq = studySeq;
        this.userSeq = userSeq;
    }

    public StudyApplyId() {
    }
}
