package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyMember;
import com.gongsp.db.entity.StudyRoomMember;
import com.gongsp.db.entity.StudyRoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StudyRoomMemberRepository  extends JpaRepository<StudyRoomMember, StudyRoomMemberId> {
    Optional<StudyRoomMember> findStudyRoomMemberByStudyRoomMemberId(StudyRoomMemberId studyRoomMemberId);
    boolean existsByStudyRoomMemberId(StudyRoomMemberId studyRoomMemberId);
    @Query(value = "select count(*) from tb_member_study " +
            "where study_seq = :studySeq and is_member_onair = 1 ",
            nativeQuery = true)
    int countStudyRoomMemberByStudySeqAndOnair(@Param(value = "studySeq")Integer studySeq);
    @Query(value = "select distinct user_seq from tb_member_study " +
            "where study_seq in " +
            "(select distinct study_seq from tb_day_study " +
            "where day_number = :dayNum " +
            "and (TIMESTAMPDIFF(MINUTE, TIME(\":curTime\"), time_start) between 1 and 10));",
            nativeQuery = true)
    List<Integer> findUserSeqByTime(@Param(value="dayNum")Integer dayNum, @Param(value="curTime") LocalTime curTime);

    @Query(value = "select user_seq from tb_member_study " +
            "where study_seq = :studySeq ; ",
            nativeQuery = true)
    List<Integer> findUserSeqByStudySeq(@Param(value="studySeq")Integer studySeq);
}
