package com.gongsp.api.service;

import com.gongsp.db.entity.Diary;
import com.gongsp.db.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service("diaryService")
public class DiaryServiceImpl implements DiaryService{

    @Autowired
    private DiaryRepository diaryRepository;

    @Override
    public Diary readDiary(Integer userSeq, LocalDate date) {
        Diary diary = diaryRepository.getDiaryByUserSeqAndDiaryDate(userSeq, date).orElse(null);
        return diary;
    }
}
