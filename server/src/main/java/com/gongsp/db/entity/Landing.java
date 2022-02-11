package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_landing")
public class Landing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer landingSeq;
    private Integer totalUser;
    private Integer totalTime;
}
