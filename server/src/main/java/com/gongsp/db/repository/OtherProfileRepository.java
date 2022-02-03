package com.gongsp.db.repository;

import com.gongsp.db.entity.OtherUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtherProfileRepository extends JpaRepository<OtherUserProfile, Integer> {
    @Query(value = "select u.user_seq as user_seq,\n" +
            " u.level_seq as user_level,\n" +
            " u.level_img_seq as user_level_image,\n" +
            " u.user_profile_msg as user_profile_message,\n" +
            " u.user_nickname as user_nickname,\n" +
            " u.user_img as user_image,\n" +
            " u.user_time_goal as user_time_goal,\n" +
            " u.user_warning as user_warning,\n" +
            " sum(t.log_meeting) + sum(t.log_study) / 7 as user_average_week_time,\n" +
            " count(distinct(case when st.study_date_end is not null and s.member_eject_count = 0 then st.study_seq end) ) as user_no_eject_study_count,\n" +
            " count(distinct(case when st.study_date_end is null then st.study_seq end)) as user_now_study_count\n" +
            "from tb_user u join tb_log_time t join tb_member_study s join tb_study st\n" +
            "where u.user_seq = :seq and t.user_seq = u.user_seq and s.user_seq = u.user_seq and log_date >= curdate() - interval 7 day and st.study_seq = s.study_seq"
            , nativeQuery = true)
    Optional<OtherUserProfile> selectOne(@Param(value = "seq") int userSeq);
}
