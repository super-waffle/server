package com.gongsp.api.service;

import com.gongsp.db.entity.BlacklistMeetingId;

public interface BlacklistMeetingService {
    public boolean isUserInBlacklist(Integer userSeq, Integer meetingSeq);
    void createBlacklist(BlacklistMeetingId blacklistMeetingId);
}
