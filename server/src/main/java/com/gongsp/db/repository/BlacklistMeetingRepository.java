package com.gongsp.db.repository;

import com.gongsp.db.entity.BlacklistMeeting;
import com.gongsp.db.entity.BlacklistMeetingId;
import com.gongsp.db.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface BlacklistMeetingRepository  extends JpaRepository<BlacklistMeeting, Integer> {
    boolean existsBlacklistMeetingByBlacklistMeetingId(BlacklistMeetingId blacklistMeetingId);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_blacklist_meeting where meeting_seq = :meetingSeq ;", nativeQuery = true)
    void deleteAllByMeetingSeq(@Param(value = "meetingSeq") int meetingSeq);
}
