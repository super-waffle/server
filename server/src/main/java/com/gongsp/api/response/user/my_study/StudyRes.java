package com.gongsp.api.response.user.my_study;

import com.gongsp.db.entity.UserStudy;
import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.entity.StudyMember;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudyRes {

    private Integer studySeq;
    private Integer ejectCount;
    private String hostName;
    private String categoryName;
    private String title;
    private String shortDescription;
    private String description;
    private String capacity;
    private String url;
    private Integer lateTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;
    private Boolean isRecruiting;
    private Boolean isOnAir;
    private StudyMember[] memberList;
    private StudyDay[] days;

    public StudyRes(UserStudy userStudy, StudyMember[] members) {
        this.studySeq = userStudy.getStudySeq();
        this.ejectCount = userStudy.getEjectCount();
        this.categoryName = userStudy.getCategoryName();
        this.title = userStudy.getTitle();
        this.shortDescription = userStudy.getShortDescription();
        this.description = userStudy.getDescription();
        this.capacity = userStudy.getCapacity();
        this.url = userStudy.getUrl();
        this.lateTime = userStudy.getLateTime();
        this.startDate = userStudy.getStartDate();
        this.endDate = userStudy.getEndDate();
        this.recruitStartDate = userStudy.getRecruitStartDate();
        this.recruitEndDate = userStudy.getRecruitEndDate();
        this.isRecruiting = userStudy.getIsRecruiting();
        this.isOnAir = userStudy.getIsOnAir();
        this.memberList = members;
        this.hostName = userStudy.getHostSeq() + "";
        for (StudyMember member : members)
            if (member.getUserSeq().equals(userStudy.getHostSeq())) {
                this.hostName = member.getUserNickname();
                break;
            }
    }

    public StudyRes(UserStudy userStudy) {
        this.studySeq = userStudy.getStudySeq();
        this.hostName = userStudy.getHostSeq() + "";
        this.ejectCount = userStudy.getEjectCount();
        this.categoryName = userStudy.getCategoryName();
        this.title = userStudy.getTitle();
        this.shortDescription = userStudy.getShortDescription();
        this.description = userStudy.getDescription();
        this.capacity = userStudy.getCapacity();
        this.url = userStudy.getUrl();
        this.lateTime = userStudy.getLateTime();
        this.startDate = userStudy.getStartDate();
        this.endDate = userStudy.getEndDate();
        this.recruitStartDate = userStudy.getRecruitStartDate();
        this.recruitEndDate = userStudy.getRecruitEndDate();
        this.isRecruiting = userStudy.getIsRecruiting();
        this.isOnAir = userStudy.getIsOnAir();
        this.memberList = null;
    }

    public void setMemberList(StudyMember[] memberList) {
        this.memberList = memberList;
        for (StudyMember member : memberList)
            if (member.getUserSeq() == Integer.parseInt(this.getHostName())) {
                this.hostName = member.getUserNickname();
                break;
            }
    }
}
