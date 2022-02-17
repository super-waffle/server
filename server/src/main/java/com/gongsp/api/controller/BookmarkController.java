package com.gongsp.api.controller;

import com.gongsp.api.response.bookmark.BookmarkListGetRes;
import com.gongsp.api.service.BookmarkService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.MeetingWithNickname;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        List<MeetingWithNickname> bookmarkList = bookmarkService.findAllByUserSeq(userSeq);
        if (bookmarkList.isEmpty()) {
            return ResponseEntity.ok(BookmarkListGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(BookmarkListGetRes.of(200, "Success", bookmarkList));
    }

    // 즐겨찾기 등록
    @PostMapping("/{meeting_seq}")
    public ResponseEntity<? extends BaseResponseBody> addBookmark(Authentication authentication, @PathVariable("meeting_seq") Integer meetingSeq) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        Boolean added = bookmarkService.addMeetingToBookmark(userSeq, meetingSeq);
        if (added) {
            return ResponseEntity.ok(BaseResponseBody.of(201, "Added to bookmark"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(400, "Failed to add meeting to bookmark"));
    }

    // 즐겨찾기 취소
    @DeleteMapping("/{meeting_seq}")
    public ResponseEntity<? extends BaseResponseBody> cancelBookmark(Authentication authentication, @PathVariable("meeting_seq") Integer meetingSeq) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        Boolean cancelled = bookmarkService.deleteMeetingFromBookmark(userSeq, meetingSeq);
        if (cancelled) {
            return ResponseEntity.ok(BaseResponseBody.of(204, "Deleted from bookmark"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(400, "Failed to delete meeting from bookmark"));
    }
}
