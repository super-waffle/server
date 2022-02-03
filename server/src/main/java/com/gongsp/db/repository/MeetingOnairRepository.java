package com.gongsp.db.repository;

import com.gongsp.db.entity.MeetingOnair;
import com.gongsp.db.entity.MeetingOnairId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingOnairRepository extends JpaRepository<MeetingOnair, MeetingOnairId> {
    Optional<MeetingOnair> findMeetingOnairByMeetingOnairId(MeetingOnairId meetingOnairId);
    boolean existsMeetingOnairByMeetingOnairId(MeetingOnairId meetingOnairId);
}
