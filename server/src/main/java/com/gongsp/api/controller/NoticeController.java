package com.gongsp.api.controller;

import com.gongsp.api.response.notice.NoticeListGetRes;
import com.gongsp.api.service.NoticeService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    // 알림 목록 조회
    @GetMapping()
    public ResponseEntity<NoticeListGetRes> getNotice(Authentication authentication, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        List<Notice> noticeList = noticeService.findByUserSeq(Integer.parseInt((String) authentication.getPrincipal()), page - 1, size);
        Integer unreadNoticeCount = noticeService.getUnreadNotice(Integer.parseInt((String) authentication.getPrincipal()));
        Integer totalPagesCount = noticeService.getTotalPagesCount(Integer.parseInt((String) authentication.getPrincipal()), page - 1, size);
        if (noticeList.isEmpty()) {
            return ResponseEntity.ok(NoticeListGetRes.of(204, "No Content", null, 0, 0));
        }
        return ResponseEntity.ok(NoticeListGetRes.of(200, "Success", noticeList, unreadNoticeCount, totalPagesCount));
    }

    // 알림 완료 토글
    @PostMapping("/{noticeSeq}")
    public ResponseEntity<? extends BaseResponseBody> toggleNotice(Authentication authentication, @PathVariable Integer noticeSeq) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Boolean toggled = noticeService.toggleNotice(Integer.parseInt((String) authentication.getPrincipal()), noticeSeq);
        if (toggled) {
            return ResponseEntity.ok(BaseResponseBody.of(200, "Notice Toggled"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(409, "Failed to toggle notice"));
    }
}
