package com.gongsp.api.service;

import com.gongsp.api.controller.NoticeController;
import com.gongsp.db.entity.Notice;
import com.gongsp.db.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    NoticeRepository noticeRepository;

    @Override
    public List<Notice> findByUserSeq(Integer userSeq, Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("noticeDate").descending());
        Page<Notice> noticePage = noticeRepository.findByUserSeq(userSeq, paging);
        List<Notice> noticeList = noticePage.getContent();
        return noticeList;
    }

    @Override
    public Boolean toggleNotice(Integer userSeq, Integer noticeSeq) {
        Notice notice = noticeRepository.findByNoticeSeq(noticeSeq).orElse(null);
        if (notice == null) {
            return false;
        }
        if (notice.getUserSeq().equals(userSeq)) {
            notice.setIsChecked(!notice.getIsChecked());
            noticeRepository.save(notice);
            return true;
        }
        return false;
    }

    @Override
    public Integer getUnreadNotice(Integer userSeq) {
        List<Notice> unreadNoticeList = noticeRepository.findAllByIsCheckedFalseAndUserSeq(userSeq);
        return unreadNoticeList.size();
    }

    @Override
    public Integer getTotalPagesCount(Integer userSeq, Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("noticeDate").descending());
        Page<Notice> noticePage = noticeRepository.findByUserSeq(userSeq, paging);
        return noticePage.getTotalPages();
    }
}
