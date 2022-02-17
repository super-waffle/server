package com.gongsp.api.response.meeting;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingDetailGetRes extends BaseResponseBody {
    private Integer meetingSeq;
    private String hostName;
    private String categoryName;
    private String meetingTitle;
    private String meetingDesc;
    private String meetingImg;
    private Integer meetingHeadcount;
    private String meetingUrl;
    private String meetingCamType;
    private String meetingMicType;
    private Boolean isMeetingOnair;
    private boolean isInBookmark;

    public static MeetingDetailGetRes of(Integer statusCode, String message, MeetingDetailGetRes meetingDetailGetRes) {
        meetingDetailGetRes.setStatusCode(statusCode);
        meetingDetailGetRes.setMessage(message);
        return meetingDetailGetRes;
    }
}
