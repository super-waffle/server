package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_bookmark")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkSeq;

    @ManyToOne
    @JoinColumn(name="user_seq")
    private User user;

    @ManyToOne
    @JoinColumn(name="meeting_seq")
    private Meeting meeting;
}
