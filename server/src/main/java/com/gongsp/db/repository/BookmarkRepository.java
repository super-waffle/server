package com.gongsp.db.repository;

import com.gongsp.db.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM tb_bookmark WHERE user_seq = :userSeq and meeting_seq = :meetingSeq")
    Bookmark findBookmarkByUserAndMeeting(@Param("userSeq") Integer userSeq, @Param("meetingSeq") Integer meetingSeq);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_bookmark where meeting_seq = :meetingSeq", nativeQuery = true)
    void deleteAllByMeetingSeq(@Param("meetingSeq") int meetingSeq);

    @Query(nativeQuery = true, value = "select user_seq from tb_bookmark " +
            "where meeting_seq = :meetingSeq " +
            "and user_seq not in (select distinct user_seq from tb_meeting_onair )" +
            "and user_seq not in (select distinct user_seq from tb_member_study where is_member_onair = 1 )")
    List<Integer> findUserByMeeting(@Param("meetingSeq") Integer meetingSeq);
}
