package com.gongsp.db.repository;

import com.gongsp.db.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Page<Notice> findByUserSeq(Integer userSeq, Pageable pageable);
    Optional<Notice> findByNoticeSeq(Integer noticeSeq);
    List<Notice> findAllByIsCheckedFalseAndUserSeq(Integer userSeq);
}
