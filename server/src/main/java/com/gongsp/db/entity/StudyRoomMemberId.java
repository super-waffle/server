package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class StudyRoomMemberId  implements Serializable {
    private Integer userSeq;
    private Integer studySeq;

    public StudyRoomMemberId(Integer userSeq, Integer studySeq) {
        this.userSeq = userSeq;
        this.studySeq = studySeq;
    }

    public StudyRoomMemberId() {
    }
}
