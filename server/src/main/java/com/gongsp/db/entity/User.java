package com.gongsp.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;
    private Integer levelSeq=1;
    private Integer levelImgSeq=1;
    private Boolean isAdmin=false;
    private String userEmail;
    private String userNickname;
    private String userProfileMsg;
    private String userImg;
    private Integer userTimeTotal;
    private Integer userTimeGoal;
    private Integer userWarning=0;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;
}
