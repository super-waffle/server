package com.gongsp.db.repository;

import com.gongsp.db.entity.MeetingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingDetailRepository extends JpaRepository<MeetingDetail, Integer> {
    Optional<MeetingDetail> findMeetingByMeetingSeq(Integer meetingSeq);
}
