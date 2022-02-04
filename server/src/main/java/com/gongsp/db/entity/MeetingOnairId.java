package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class MeetingOnairId implements Serializable {
    private Integer meetingSeq;
    private Integer userSeq;

    public MeetingOnairId(Integer userSeq, Integer meetingSeq) {
        this.meetingSeq = meetingSeq;
        this.userSeq = userSeq;
    }

    public MeetingOnairId() { }
}
