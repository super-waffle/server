package com.gongsp.db.repository;

import com.gongsp.db.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    Optional<Meeting> findMeetingByMeetingSeq(Integer meetingSeq);
    boolean existsMeetingByMeetingSeq(Integer meetingSeq);
}