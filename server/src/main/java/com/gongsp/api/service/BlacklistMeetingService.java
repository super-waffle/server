package com.gongsp.api.service;

public interface BlacklistMeetingService {
    public boolean isUserInBlacklist(Integer userSeq, Integer meetingSeq);
}
