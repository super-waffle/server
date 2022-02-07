package com.gongsp.api.service;

import com.gongsp.db.entity.BlacklistMeeting;
import com.gongsp.db.entity.BlacklistMeetingId;
import com.gongsp.db.repository.BlacklistMeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("blacklistMeetingService")
public class BlacklistMeetingServiceImpl implements BlacklistMeetingService{

    @Autowired
    BlacklistMeetingRepository blacklistMeetingRepository;
    
    @Override
    public boolean isUserInBlacklist(Integer userSeq, Integer meetingSeq) {
        return blacklistMeetingRepository.existsBlacklistMeetingByBlacklistMeetingId(new BlacklistMeetingId(userSeq, meetingSeq));
    }

    @Override
    public void createBlacklist(BlacklistMeetingId blacklistMeetingId) {
        blacklistMeetingRepository.save(new BlacklistMeeting(blacklistMeetingId));
    }
}
