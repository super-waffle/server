package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@ToString
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
