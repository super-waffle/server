package com.gongsp.api.response.user.my_study;

import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.entity.StudyMember;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudyRes {

    private Integer studySeq;
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

    public StudyRes(Study study, StudyMember[] members) {
        this.studySeq = study.getStudySeq();
        this.categoryName = study.getCategoryName();
        this.title = study.getTitle();
        this.shortDescription = study.getShortDescription();
        this.description = study.getDescription();
        this.capacity = study.getCapacity();
        this.url = study.getUrl();
        this.lateTime = study.getLateTime();
        this.startDate = study.getStartDate();
        this.endDate = study.getEndDate();
        this.recruitStartDate = study.getRecruitStartDate();
        this.recruitEndDate = study.getRecruitEndDate();
        this.isRecruiting = study.getIsRecruiting();
        this.isOnAir = study.getIsOnAir();
        this.memberList = members;
        this.hostName = study.getHostSeq() + "";
        for (StudyMember member : members)
            if (member.getUserSeq().equals(study.getHostSeq())) {
                this.hostName = member.getUserNickname();
                break;
            }
    }

    public StudyRes(Study study) {
        this.studySeq = study.getStudySeq();
        this.hostName = study.getHostSeq() + "";
        this.categoryName = study.getCategoryName();
        this.title = study.getTitle();
        this.shortDescription = study.getShortDescription();
        this.description = study.getDescription();
        this.capacity = study.getCapacity();
        this.url = study.getUrl();
        this.lateTime = study.getLateTime();
        this.startDate = study.getStartDate();
        this.endDate = study.getEndDate();
        this.recruitStartDate = study.getRecruitStartDate();
        this.recruitEndDate = study.getRecruitEndDate();
        this.isRecruiting = study.getIsRecruiting();
        this.isOnAir = study.getIsOnAir();
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
