package com.gongsp.api.response.study;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyMemberRes {
    String userNickname;
    Integer isAttend; // 0:결석, 1:지각, 2:출석
    Boolean isHost;

    public StudyMemberRes(String userNickname, Integer isAttend, Boolean isHost) {
        this.userNickname = userNickname;
        this.isAttend = isAttend;
        this.isHost = isHost;
    }

    public StudyMemberRes() {
    }
}
