package com.gongsp.api.response.notice;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Notice;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoticeListGetRes extends BaseResponseBody {
    private List<Notice> noticeList;
    private Integer unreadNoticeCount;

    public static com.gongsp.api.response.notice.NoticeListGetRes of(Integer statusCode, String message, List<Notice> noticeList, Integer unreadNoticeCount) {
        com.gongsp.api.response.notice.NoticeListGetRes res = new com.gongsp.api.response.notice.NoticeListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setNoticeList(noticeList);
        res.setUnreadNoticeCount(unreadNoticeCount);
        return res;
    }
}