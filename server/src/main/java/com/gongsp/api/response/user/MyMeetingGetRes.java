package com.gongsp.api.response.user;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyMeetingGetRes extends BaseResponseBody {
    private Meeting meetingInfo;

    public static MyMeetingGetRes of(int statusCode, String message, Meeting meeting) {
        MyMeetingGetRes result = new MyMeetingGetRes();
        result.setStatusCode(statusCode);
        result.setMeetingInfo(meeting);
        result.setMessage(message);
        return result;
    }
}
