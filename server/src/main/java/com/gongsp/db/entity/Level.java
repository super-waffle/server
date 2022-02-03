package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "tb_level")
public class Level {

    @Id
    private Integer levelSeq;
    private String levelName;
    private String levelImg;
}
