package com.gongsp.api.service;

import com.gongsp.api.request.diary.DiaryContentInfo;
import com.gongsp.api.request.diary.DiaryCreatePostReq;
import com.gongsp.db.entity.Diary;

import java.time.LocalDate;

public interface DiaryService {
    Diary readDiary(Integer userSeq, LocalDate date);
    Boolean deleteDiary(Integer userSeq, Integer diarySeq);
    Boolean createDiary(Integer userSeq, DiaryCreatePostReq request, String uuidFilename);
    Boolean updateDiary(Integer userSeq, Integer diarySeq, DiaryContentInfo contentInfo, String uuidFilename);
    Boolean existsDiary(Integer diarySeq);
}
