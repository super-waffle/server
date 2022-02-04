package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_meeting_onair")
public class MeetingOnair {
    @EmbeddedId
    private MeetingOnairId meetingOnairId;
    private Boolean isHost;
}
