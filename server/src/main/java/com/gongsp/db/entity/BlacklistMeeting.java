package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="tb_blacklist_meeting")
public class BlacklistMeeting {
    @EmbeddedId
    private BlacklistMeetingId blacklistMeetingId;

    public BlacklistMeeting(BlacklistMeetingId blacklistMeetingId) {
        this.blacklistMeetingId = blacklistMeetingId;
    }

    public BlacklistMeeting() {
    }
}
