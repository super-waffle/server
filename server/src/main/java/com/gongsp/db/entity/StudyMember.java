package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "tb_member_study")
public class StudyMember {

    @Id
    private Integer userSeq;
    private String userNickname;
    @Column(name = "member_eject_count")
    private Integer ejectCount;
    @Column(name = "is_member_onair")
    private Boolean isOnAir;
}
