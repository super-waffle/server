package com.gongsp.db.repository;

import com.gongsp.db.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    Level getLevelByLevelSeq(Integer levelSeq);
}
