package com.gongsp.db.repository;

import com.gongsp.db.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Meeting, Integer> {
    
    // 즐겨찾기한 자유열람실 목록 조회
    @Query(nativeQuery = true, value = "(SELECT * FROM tb_meeting WHERE meeting_seq in " +
            "(SELECT meeting_seq FROM tb_bookmark WHERE user_seq = :userSeq)) ")
    List<Meeting> findAllByUser(@Param("userSeq") Integer userSeq);

}
