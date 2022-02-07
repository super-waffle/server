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
@Table(name="tb_apply_study")
public class StudyApply {
    @EmbeddedId
    StudyApplyId studyApplyId;

    public StudyApply(StudyApplyId studyApplyId) {
        this.studyApplyId = studyApplyId;
    }

    public StudyApply() {
    }
}
