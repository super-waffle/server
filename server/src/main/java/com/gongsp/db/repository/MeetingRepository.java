package com.gongsp.db.repository;

import com.gongsp.db.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    Optional<Meeting> findMeetingByMeetingSeq(Integer meetingSeq);

//    boolean existsMeetingByMeetingSeq(Integer meetingSeq);

//    Optional<List<Meeting>> findMeetingsByMeetingSeq(Integer meetingSeq, Pageable pageable);

    //    Page<Meeting> findAll(Pageable pageable);
    @Query(nativeQuery = true, value = "select * from tb_meeting m " +
            "order by m.meeting_seq desc limit :start, :spp ")
    List<Meeting> searchAll(@Param("start") Integer start, @Param("spp") Integer spp);

    //    Optional<List<Meeting>> findByMeetingTitleContainingOrMeetingDescContaining(String meetingTitle, String meetingDesc, Pageable pageable);
    @Query(nativeQuery = true, value = "select * from tb_meeting m " +
            "where m.category_seq = :categorySeq " +
            "order by m.meeting_seq desc limit :start, :spp ")
    List<Meeting> searchByCategorySeq(@Param("categorySeq") Integer categorySeq, @Param("start") Integer start, @Param("spp") Integer spp);
//    Optional<List<Meeting>> findByMeetingTitleContainingOrMeetingDescContainingAndCategorySeq(String meetingTitle, String meetingDesc, Integer categorySeq, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from tb_meeting m " +
            "where (m.meeting_title like %:key% or m.meeting_desc like %:key% ) " +
            "order by m.meeting_seq desc limit :start, :spp ")
    List<Meeting> searchByKey(@Param("key") String key, @Param("start") Integer start, @Param("spp") Integer spp);

    @Query(nativeQuery = true, value = "select * from tb_meeting m " +
            "where (m.meeting_title like %:key% or m.meeting_desc like %:key% ) " +
            "and m.category_seq = :categorySeq " +
            "order by m.meeting_seq desc limit :start, :spp ")
    List<Meeting> searchByKeyAndCategory(@Param("key") String key, @Param("categorySeq") Integer categorySeq, @Param("start") Integer start, @Param("spp") Integer spp);
}