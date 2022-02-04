package com.gongsp.api.controller;

import com.gongsp.api.response.diary.DiaryReadGetRes;
import com.gongsp.api.service.DiaryService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Diary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/diaries")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping()
    public ResponseEntity<DiaryReadGetRes> readDiary(Authentication authentication, @RequestParam("date") String date) {
        Diary diary = diaryService.readDiary(Integer.parseInt((String) authentication.getPrincipal()), LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
        if (diary == null) {
            return ResponseEntity.ok(DiaryReadGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(DiaryReadGetRes.of(200, "Success", diary));
    }
}
