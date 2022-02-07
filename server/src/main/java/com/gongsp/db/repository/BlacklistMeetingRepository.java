package com.gongsp.db.repository;

import com.gongsp.db.entity.BlacklistMeeting;
import com.gongsp.db.entity.BlacklistMeetingId;
import com.gongsp.db.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistMeetingRepository  extends JpaRepository<BlacklistMeeting, Integer> {
    boolean existsBlacklistMeetingByBlacklistMeetingId(BlacklistMeetingId blacklistMeetingId);
}
