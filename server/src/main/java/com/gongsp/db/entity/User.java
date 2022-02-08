package com.gongsp.db.entity;

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

    @OneToOne
    @JoinColumn (name = "level_seq", updatable = false, insertable = false)
    private Level userLevel;

    @OneToOne
    @JoinColumn (name = "level_img_seq", updatable = false, insertable = false)
    private Level userImageLevel;
    private Boolean isAdmin;
    private String userEmail;
    private String userNickname;
    private String userProfileMsg;
    private String userImg;
    private Integer userTimeTotal;
    private Integer userTimeGoal;
    private Integer userWarning;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;

    public User(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public User() {
    }
}
