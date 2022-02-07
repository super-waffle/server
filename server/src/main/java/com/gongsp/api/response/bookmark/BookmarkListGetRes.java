package com.gongsp.api.response.bookmark;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookmarkListGetRes extends BaseResponseBody {
    private List<Meeting> bookmarkList;

    public static com.gongsp.api.response.bookmark.BookmarkListGetRes of(Integer statusCode, String message, List<Meeting> bookmarkList) {
        com.gongsp.api.response.bookmark.BookmarkListGetRes res = new com.gongsp.api.response.bookmark.BookmarkListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setBookmarkList(bookmarkList);
        return res;
    }
}