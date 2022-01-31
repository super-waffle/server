package com.gongsp.api.service;

import com.gongsp.db.entity.Notice;

import java.util.List;


public interface NoticeService {
    List<Notice> findByUserSeq(Integer userSeq, Integer page, Integer size);
}
