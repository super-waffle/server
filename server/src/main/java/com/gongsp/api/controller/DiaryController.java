package com.gongsp.api.controller;

import com.gongsp.api.request.diary.DiaryCreatePostReq;
import com.gongsp.api.response.diary.DiaryReadGetRes;
import com.gongsp.api.service.DiaryService;
import com.gongsp.api.service.StorageService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Diary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/diaries")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private StorageService storageService;

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

    // 하루기록 생성
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> createDiary(Authentication authentication, @ModelAttribute DiaryCreatePostReq request) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }

        // 이미지 파일 저장
        if (request.getImage() == null) {
            return ResponseEntity.ok(DiaryReadGetRes.of(400, "Failed to upload image"));
        }
        String uuidFilename = imageUpload(request.getImage());

        // 하루기록 내용 저장
        Boolean diaryCreated = diaryService.createDiary(Integer.parseInt((String) authentication.getPrincipal()), request, uuidFilename);
        if (!diaryCreated) {
            return ResponseEntity.ok(DiaryReadGetRes.of(400, "Failed to create diary"));
        }
        return ResponseEntity.ok(DiaryReadGetRes.of(201, "Diary Created"));

    }

    // 이미지 파일 업로드
    @PutMapping()
    public String imageUpload(MultipartFile file) {
        // 파일을 경로에 저장
        String uuidFilename = storageService.store(file);
        return uuidFilename;
    }
}
