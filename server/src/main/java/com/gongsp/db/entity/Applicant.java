package com.gongsp.db.entity;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "tb_apply_study")
public class Applicant {

    private Integer studySeq;
    @Id
    @Ignore
    private Integer userSeq;

    @OneToOne
    @JoinColumn(name = "user_seq", updatable = false, insertable = false)
    private User applicant;
}
