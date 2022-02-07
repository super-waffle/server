package com.gongsp.api.service;

import com.gongsp.db.entity.Meeting;

import java.util.List;

public interface BookmarkService {
    List<Meeting> findAllByUserSeq(Integer userSeq);
    Boolean addMeetingToBookmark(Integer userSeq, Integer meetingSeq);
    Boolean deleteMeetingFromBookmark(Integer userSeq, Integer meetingSeq);
}
