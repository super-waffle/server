package com.gongsp.api.service;

import com.gongsp.db.entity.Notice;
import com.gongsp.db.entity.User;
import com.gongsp.db.entity.UserAchieve;
import com.gongsp.db.repository.NoticeRepository;
import com.gongsp.db.repository.UserAchieveRepository;
import com.gongsp.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAchieveRepository userAchieveRepository;

    @Autowired
    private AchieveService achieveService;

    @Autowired
    private SseService sseService;

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

    // 업적 알림 전송 & 알림보관함에 저장
    @Override
    public void sendAchieveNotice(Integer userSeq, Integer achieveSeq, String achieveTitle) {
        User user = userRepository.findUserByUserSeq(userSeq).orElse(null);
        UserAchieve existingAchieve = userAchieveRepository.findByAchieveAndUser(userSeq, achieveSeq).orElse(null);
        if (user != null && existingAchieve == null) {
            // 유저업적 테이블에 등록
            achieveService.addAchieve(user, achieveSeq);
            // 실시간 알림 전송 및 알림 보관함에 알림 등록
            sseService.sendAchieveNotice(userSeq, "신규 업적이 등록되었습니다: [" + achieveTitle + "]");
        }
    }
}
