package com.gongsp.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userSeq;
    private Integer levelSeq;
    private Integer levelImgSeq;
    private Boolean isAdmin;
    private String userEmail;
    private String userNickname;
    private String userProfileMsg;
    private String userImg;
    private Integer userTimeTotal;
    private Integer userTimeGoal;
    private Integer userWarning;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;
}
