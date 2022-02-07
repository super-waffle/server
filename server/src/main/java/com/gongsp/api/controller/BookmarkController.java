package com.gongsp.api.controller;

import com.gongsp.api.response.bookmark.BookmarkListGetRes;
import com.gongsp.api.response.notice.NoticeListGetRes;
import com.gongsp.db.entity.Bookmark;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    // 즐겨찾기 목록 조회
    @GetMapping()
    public ResponseEntity<BookmarkListGetRes> getBookmarkList(Authentication authentication, @RequestParam("page") Integer page) {
        List<Bookmark> bookmarkList = bookmarkService.findByUserSeq(Integer.parseInt((String) authentication.getPrincipal()), page-1, size);
        Integer unreadNoticeCount = noticeService.getUnreadNotice(Integer.parseInt((String) authentication.getPrincipal()));
        if (noticeList.isEmpty()) {
            return ResponseEntity.ok(NoticeListGetRes.of(204, "No Content", null, 0));
        }
        return ResponseEntity.ok(NoticeListGetRes.of(200, "Success", noticeList, unreadNoticeCount));
    }
}
