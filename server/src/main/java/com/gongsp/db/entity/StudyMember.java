package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class StudyMember {

    @Id
    private Integer userSeq;
    private String userNickname;
}
