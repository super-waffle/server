package com.gongsp.db.repository;

import com.gongsp.db.entity.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Integer> {
    Optional<StudyRoom> findStudyByStudySeq(Integer studySeq);

    @Query(nativeQuery = true, value = "select count(*) from tb_study " +
            "where is_study_recruiting = true " +
            "and study_recruit_end >= :today ;")
    int countAllWithoutCount(@Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "select * from tb_study " +
            "where is_study_recruiting = true " +
            "and study_recruit_end >= :today " +
            "order by study_seq desc limit :start, :spp ;")
    List<StudyRoom> searchAll(@Param("start") Integer start, @Param("spp") Integer spp, @Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "select count(*) from tb_study " +
            "where is_study_recruiting = true " +
            "and study_recruit_end >= :today " +
            "and category_seq = :categorySeq ;")
    int countByCategory(@Param("categorySeq") int categorySeq, @Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "select * from tb_study " +
            "where category_seq = :categorySeq " +
            "and is_study_recruiting = true " +
            "and study_recruit_end >= :today " +
            "order by study_seq desc limit :start, :spp ;")
    List<StudyRoom> searchByCategorySeq(@Param("categorySeq") Integer categorySeq, @Param("start") Integer start, @Param("spp") Integer spp, @Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "select count(*) from tb_study " +
            "where (study_title like %:key% or study_desc like %:key% or study_short_desc like %:key% ) " +
            "and study_recruit_end >= :today " +
            "and is_study_recruiting = true ;")
    int countByLike(@Param("key") String key, @Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "select * from tb_study " +
            "where (study_title like %:key% or study_desc like %:key% or study_short_desc like %:key% ) " +
            "and is_study_recruiting = true " +
            "and study_recruit_end >= :today " +
            "order by study_seq desc limit :start, :spp ;")
    List<StudyRoom> searchByKey(@Param("key") String key, @Param("start") Integer start, @Param("spp") Integer spp, @Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "select count(*) from tb_study " +
            "where (study_title like %:key% or study_desc like %:key% or study_short_desc like %:key% ) " +
            "and is_study_recruiting = true " +
            "and study_recruit_end >= :today " +
            "and category_seq = :categorySeq ;")
    int countByLikeAndCategory(@Param("categorySeq") int categorySeq, @Param("key") String key, @Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "select * from tb_study " +
            "where (study_title like %:key% or study_desc like %:key% or study_short_desc like %:key% ) " +
            "and category_seq = :categorySeq " +
            "and is_study_recruiting = true " +
            "and study_recruit_end >= :today " +
            "order by study_seq desc limit :start, :spp ;")
    List<StudyRoom> searchByKeyAndCategory(@Param("key") String key, @Param("categorySeq") Integer categorySeq, @Param("start") Integer start, @Param("spp") Integer spp, @Param("today") LocalDate today);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_study WHERE study_recruit_end <= :date ;")
    List<StudyRoom> findStudyRoomsByStudyDateEnd(@Param("date") LocalDate date);
}
