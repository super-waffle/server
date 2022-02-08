package com.gongsp.db.repository;

import com.gongsp.db.entity.UserAchieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AchieveRepository extends JpaRepository<UserAchieve, Integer> {

    @Query(value = "SELECT *\n" +
            "FROM tb_user_achieve u\n" +
            "JOIN tb_achieve a\n" +
            "ON u.achieve_seq = a.achieve_seq\n" +
            "WHERE u.user_seq = :userSeq ;",
            nativeQuery = true)
    List<UserAchieve> findAllByUserSeq(@Param(value = "userSeq") Integer userSeq);
}
