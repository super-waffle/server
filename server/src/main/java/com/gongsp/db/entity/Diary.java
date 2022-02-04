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
@Table(name="tb_diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diarySeq;
    private Integer userSeq;
    private LocalDate diaryDate;
    private String diaryContent;
    private String diaryImg;
}
