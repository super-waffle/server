package com.gongsp.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@Table(name = "tb_member_study")
public class StudyRoomMember {
    @EmbeddedId
    StudyRoomMemberId studyRoomMemberId;

    @Column(name = "member_eject_count")
    private Integer memberEjectCount;
    @Column(name = "is_member_onair")
    private Boolean isMemberOnAir;

    public StudyRoomMember(StudyRoomMemberId studyRoomMemberId, Integer ejectCount, Boolean isOnAir) {
        this.studyRoomMemberId = studyRoomMemberId;
        this.memberEjectCount = ejectCount;
        this.isMemberOnAir = isOnAir;
    }

    public StudyRoomMember() {
    }
}
