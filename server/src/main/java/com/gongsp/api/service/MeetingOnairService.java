package com.gongsp.api.service;

import com.gongsp.db.entity.MeetingOnair;

public interface MeetingOnairService {
    void deleteOnair(Integer userSeq, Integer meetingSeq);
    MeetingOnair createOnair(Integer userSeq, Integer meetingSeq, Boolean isHost);
    MeetingOnair createOnair(Integer userSeq, Integer meetingSeq);
    boolean existsOnair(Integer userSeq, Integer meetingSeq);
}
