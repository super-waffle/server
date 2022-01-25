package com.gongsp.db.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userSeq;
    private Integer levelSeq;
    private Integer levelImgSeq;
    private boolean isAdmin;
    private String userEmail;
    private String userNickname;
    private String userPassword;
    private String userProfileMsg;
    private String userImg;
    private int userTimeTotal;
    private int userTimeGoal;
    private int userWarning;
}
