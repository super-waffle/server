package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyMember;
import com.gongsp.db.entity.StudyRoomMember;
import com.gongsp.db.entity.StudyRoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudyRoomMemberRepository  extends JpaRepository<StudyRoomMember, StudyRoomMemberId> {
    Optional<StudyRoomMember> findStudyRoomMemberByStudyRoomMemberId(StudyRoomMemberId studyRoomMemberId);
    boolean existsByStudyRoomMemberId(StudyRoomMemberId studyRoomMemberId);
    @Query(value = "select count(*) from tb_member_study " +
            "where study_seq = :studySeq and is_member_onair = 1 ",
            nativeQuery = true)
    int countStudyRoomMemberByStudySeqAndOnair(@Param(value = "studySeq")Integer studySeq);
}
