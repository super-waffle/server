package com.gongsp.db.repository;

import com.gongsp.db.entity.Achieve;
import com.gongsp.db.entity.User;
import com.gongsp.db.entity.UserAchieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAchieveRepository extends JpaRepository<UserAchieve, Integer> {

    @Query(value = "SELECT *\n" +
            "FROM tb_user_achieve u\n" +
            "JOIN tb_achieve a\n" +
            "ON u.achieve_seq = a.achieve_seq\n" +
            "WHERE u.user_seq = :userSeq ;",
            nativeQuery = true)
    List<UserAchieve> findAllByUserSeq(@Param(value = "userSeq") Integer userSeq);

    @Query(value = "SELECT *\n" +
            "FROM tb_user_achieve\n" +
            "WHERE user_seq = :userSeq AND achieve_seq = :achieveSeq ;",
            nativeQuery = true)
    Optional<UserAchieve> findByAchieveAndUser(@Param(value="userSeq") Integer userSeq, @Param(value="achieveSeq") Integer achieveSeq);

    @Query(value = "SELECT *\n" +
            "FROM tb_user_achieve\n" +
            "WHERE user_seq = :userSeq AND is_achieve_active = true;",
            nativeQuery = true)
    Optional<UserAchieve> findActivatedAchieveByUser(@Param(value="userSeq") Integer userSeq);

    Boolean existsUserAchieveByUserAndAchieve(User user, Achieve achieve);
}
