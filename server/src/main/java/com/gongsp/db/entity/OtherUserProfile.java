package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class OtherUserProfile {

    @Id
    private Integer userSeq;

    @OneToOne
    @JoinColumn(name = "user_level")
    private Level userLevel;

    @OneToOne
    @JoinColumn(name = "user_level_image")
    private Level userLevelImage;

    private String userProfileMessage;
    private String userNickname;
    private String userImage;
    private Integer userTimeGoal;
    private Integer userWarning;
    private Double userAverageWeekTime;
    private Integer userNoEjectStudyCount;
    private Integer userNowStudyCount;
}
