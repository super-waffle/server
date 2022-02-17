package com.gongsp.api.response.meeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingRes {
    private int meetingSeq;
    private String categoryName;
    private String meetingTitle;
    private String meetingImg;
    private int meetingHeadcount;
    private boolean isMeetingOnair;
    private boolean isInBookmark;
}
