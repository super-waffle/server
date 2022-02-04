package com.gongsp.api.service;

import com.gongsp.db.entity.Diary;

import java.time.LocalDate;

public interface DiaryService {
    Diary readDiary(Integer userSeq, LocalDate date);
}
