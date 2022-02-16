package com.gongsp.db.repository;

import com.gongsp.db.entity.Achieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AchieveRepository extends JpaRepository<Achieve, Integer> {
    Achieve findByAchieveSeq(Integer achieveSeq);

    @Query(value = "SELECT achieve_seq FROM tb_user_achieve WHERE user_seq = :userSeq ;", nativeQuery = true)
    List<Integer> findAllAchieveSeqByUserSeq(@Param(value="userSeq") Integer userSeq);

    @Query(value = "SELECT * FROM tb_achieve;", nativeQuery = true)
    List<Achieve> getAll();
}
