package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_achieve")
public class Achieve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achieveSeq;
    private String achieveName;
    private String achieveContent;
    private String achieveImg;
}
