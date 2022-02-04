package com.gongsp.api.controller;

import com.gongsp.api.response.diary.DiaryReadGetRes;
import com.gongsp.api.service.DiaryService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Diary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/diaries")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    // 하루기록 조회
    @GetMapping()
    public ResponseEntity<DiaryReadGetRes> readDiary(Authentication authentication, @RequestParam("date") String date) {
        Diary diary = diaryService.readDiary(Integer.parseInt((String) authentication.getPrincipal()), LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
        if (diary == null) {
            return ResponseEntity.ok(DiaryReadGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(DiaryReadGetRes.of(200, "Diary Created", diary));
    }

    // 하루기록 삭제
    @DeleteMapping("/{diary_seq}")
    public ResponseEntity<? extends BaseResponseBody> deleteDiary(Authentication authentication, @PathVariable("diary_seq") Integer diarySeq) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Boolean diaryDeleted = diaryService.deleteDiary(Integer.parseInt((String) authentication.getPrincipal()), diarySeq);
        if (diaryDeleted) {
            return ResponseEntity.ok(DiaryReadGetRes.of(204, "Diary Deleted"));
        }
        return ResponseEntity.ok(DiaryReadGetRes.of(404, "Failed to delete diary"));
    }
}
