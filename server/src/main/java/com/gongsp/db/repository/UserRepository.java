package com.gongsp.db.repository;

import com.gongsp.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUserEmail(String userEmail);
    Optional<User> findUserByUserSeq(Integer userSeq);
    Boolean existsUserByUserSeq(Integer userSeq);
    Optional<User> findUserByUserNickname(String userNickname);

    @Query(value = "SELECT COUNT(*) FROM tb_user;", nativeQuery = true)
    Integer getUserCount();

    @Query(value = "SELECT user_time_goal FROM tb_user WHERE user_seq = :userSeq ;", nativeQuery = true)
    Integer getUserTimeGoal(@Param(value="userSeq") Integer userSeq);

    @Query(value = "SELECT u.user_seq, u.user_time_total, l.level_condition\n" +
            "FROM tb_user u\n" +
            "JOIN tb_level l ON u.level_seq + 1 = l.level_seq\n" +
            "WHERE u.user_seq = :userSeq ;", nativeQuery = true)
    List<Object[]> getNextLevelConditionByUserSeq(@Param(value = "userSeq") Integer userSeq);


    @Query(value = "SELECT user_nickname\n" +
            "FROM tb_user\n" +
            "WHERE user_seq = :userSeq ;", nativeQuery = true)
    String findUserNicknameByUserSeq(@Param(value = "userSeq") Integer userSeq);
}