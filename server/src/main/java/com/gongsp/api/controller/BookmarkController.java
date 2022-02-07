package com.gongsp.api.controller;

import com.gongsp.api.response.bookmark.BookmarkListGetRes;
import com.gongsp.api.service.BookmarkService;
import com.gongsp.db.entity.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    // 즐겨찾기 목록 조회
    @GetMapping()
    public ResponseEntity<BookmarkListGetRes> getBookmarkList(Authentication authentication) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        List<Meeting> bookmarkList = bookmarkService.findAllByUserSeq(userSeq);
        if (bookmarkList.isEmpty()) {
            return ResponseEntity.ok(BookmarkListGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(BookmarkListGetRes.of(200, "Success", bookmarkList));
    }
}
