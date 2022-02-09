package com.gongsp.api.service;

import com.gongsp.db.entity.Notice;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


public interface NoticeService {
    List<Notice> findByUserSeq(Integer userSeq, Integer page, Integer size);
    Boolean toggleNotice(Integer userSeq, Integer noticeSeq);
    Integer getUnreadNotice(Integer userSeq);
    Integer getTotalPagesCount(Integer userSeq, Integer page, Integer size);
}
