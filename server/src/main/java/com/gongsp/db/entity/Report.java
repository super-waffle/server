package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportSeq;
    private Integer userSeqFrom;
    private Integer userSeqTo;
    private String reportContent;
    private LocalDateTime reportDate;
}
