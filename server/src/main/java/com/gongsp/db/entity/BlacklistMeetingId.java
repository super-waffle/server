package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class BlacklistMeetingId  implements Serializable {
    private Integer userSeq;
    private Integer meetingSeq;

    public BlacklistMeetingId(Integer userSeq, Integer meetingSeq) {
        this.userSeq = userSeq;
        this.meetingSeq = meetingSeq;
    }

    public BlacklistMeetingId() {
    }
}

