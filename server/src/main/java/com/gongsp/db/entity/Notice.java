package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeSeq;
    private Integer userSeq;
    private Integer categorySeq;
    private LocalDate noticeDate;
    private String noticeContent;
    private Boolean isChecked;

    public Notice() {
    }

    public Notice(Integer userSeq, Integer categorySeq, LocalDate noticeDate, String noticeContent, Boolean isChecked) {
        this.userSeq = userSeq;
        this.noticeDate = noticeDate;
        this.categorySeq = categorySeq;
        this.noticeContent = noticeContent;
        this.isChecked = isChecked;
    }
}
