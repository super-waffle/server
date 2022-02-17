package com.gongsp.api.service;

import com.gongsp.db.entity.MeetingWithNickname;

import java.util.List;

public interface BookmarkService {
    List<MeetingWithNickname> findAllByUserSeq(Integer userSeq);
    Boolean addMeetingToBookmark(Integer userSeq, Integer meetingSeq);
    Boolean deleteMeetingFromBookmark(Integer userSeq, Integer meetingSeq);
    List<Integer> findUserByMeetingSeq(Integer meetingSeq);
}
