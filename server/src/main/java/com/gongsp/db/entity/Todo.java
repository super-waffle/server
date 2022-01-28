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
@Table(name="tb_todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer todoSeq;
    private Integer userSeq;
    private LocalDate todoDate;
    private String todoContent;
    private Boolean todoCompleted;
}
