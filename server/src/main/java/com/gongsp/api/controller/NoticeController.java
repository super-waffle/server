package com.gongsp.api.controller;

import com.gongsp.api.response.notice.NoticeListGetRes;
import com.gongsp.api.service.NoticeService;
import com.gongsp.db.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @GetMapping()
    public ResponseEntity<NoticeListGetRes> getNotice(Authentication authentication, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        // notice 테이블에 userSeq로 접근을 해서 모든 데이터를 다 가져오기
        List<Notice> noticeList = noticeService.findByUserSeq(Integer.parseInt((String) authentication.getPrincipal()), page-1, size);

        if (noticeList.isEmpty()) {
            return ResponseEntity.ok(NoticeListGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(NoticeListGetRes.of(200, "Success", noticeList));
    }
}
