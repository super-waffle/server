package com.gongsp.db.repository;

import com.gongsp.db.entity.MeetingWithNickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingWithNicknameRepository extends JpaRepository<MeetingWithNickname, Integer> {

    @Query(nativeQuery = true, value = "SELECT m.*, u.user_nickname FROM tb_meeting m\n" +
            "JOIN tb_user u ON (u.user_seq = m.host_seq)\n" +
            "WHERE meeting_seq in\n" +
            "(SELECT meeting_seq FROM tb_bookmark WHERE user_seq = :userSeq);")
    List<MeetingWithNickname> findAllByUserWithNickname(@Param("userSeq") Integer userSeq);
}
