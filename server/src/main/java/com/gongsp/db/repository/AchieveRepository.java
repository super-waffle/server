package com.gongsp.db.repository;

import com.gongsp.db.entity.Achieve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchieveRepository extends JpaRepository<Achieve, Integer> {
    Achieve findByAchieveSeq(Integer achieveSeq);
}
