package com.gongsp.db.repository;

import com.gongsp.db.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Page<Notice> findByUserSeq(Integer userSeq, Pageable pageable);
}
