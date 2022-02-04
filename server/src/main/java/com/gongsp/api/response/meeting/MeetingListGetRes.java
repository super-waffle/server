package com.gongsp.api.response.meeting;

import com.gongsp.api.request.meeting.MeetingParameter;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MeetingListGetRes extends BaseResponseBody {
    private int totalPage;
    private int currentPage;
    private int type;
    private String key;
    private List<MeetingRes> data;

    public static MeetingListGetRes of(Integer statusCode, String message, MeetingParameter meetingParameter, int totalPage, List<MeetingRes> data) {
        MeetingListGetRes res = new MeetingListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setTotalPage(totalPage);
        res.setCurrentPage(meetingParameter.getPage());
        res.setType(meetingParameter.getType());
        res.setKey(meetingParameter.getKey());
        res.setData(data);
        return res;
    }
}
