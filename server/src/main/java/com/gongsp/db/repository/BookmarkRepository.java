package com.gongsp.db.repository;

import com.gongsp.db.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

    // user_seq와 meeting_seq로 북마크 찾기
    @Query(nativeQuery = true, value = "SELECT bookmark_seq FROM tb_bookmark WHERE user_seq = :userSeq and meeting_seq = :meetingSeq")
    Integer findBookmarkByUserAndMeeting(@Param("userSeq") Integer userSeq, @Param("meetingSeq") Integer meetingSeq);
}
