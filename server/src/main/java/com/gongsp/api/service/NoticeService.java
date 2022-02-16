package com.gongsp.api.service;

import com.gongsp.db.entity.Notice;

import java.util.List;


public interface NoticeService {
    List<Notice> findByUserSeq(Integer userSeq, Integer page, Integer size);
    Boolean toggleNotice(Integer userSeq, Integer noticeSeq);
    Integer getUnreadNotice(Integer userSeq);
    Integer getTotalPagesCount(Integer userSeq, Integer page, Integer size);
    void sendAchieveNotice(Integer userSeq, Integer achieveSeq, String achieveTitle);
}
