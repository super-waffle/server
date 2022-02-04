package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meetingSeq;
    private Integer hostSeq;
    private Integer categorySeq;
    private String meetingTitle;
    private String meetingDesc;
    private String meetingImg;
    private Integer meetingCapacity;
    private Integer meetingHeadcount;
    private String meetingUrl;
    // 0 : X, 1: 얼굴, 2: 손, 3: 노캠
    private Short meetingCamType;
    // 0 : X, 1: 음소거, 2: 소음
    private Short meetingMicType;
    private Boolean isMeetingOnair;
}