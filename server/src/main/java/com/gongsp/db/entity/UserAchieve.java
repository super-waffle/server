package com.gongsp.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_user_achieve")
public class UserAchieve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer userAchieveSeq;

    @ManyToOne
    @JoinColumn(name="user_seq")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name="achieve_seq")
    private Achieve achieve;

    private Boolean isAchieveActive;
}
