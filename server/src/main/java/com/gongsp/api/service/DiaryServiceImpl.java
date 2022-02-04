package com.gongsp.api.service;

import com.gongsp.api.request.diary.DiaryCreatePostReq;
import com.gongsp.db.entity.Diary;
import com.gongsp.db.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service("diaryService")
public class DiaryServiceImpl implements DiaryService{

    @Autowired
    private DiaryRepository diaryRepository;

    @Override
    public Diary readDiary(Integer userSeq, LocalDate date) {
        Diary diary = diaryRepository.getDiaryByUserSeqAndDiaryDate(userSeq, date).orElse(null);
        return diary;
    }

    @Override
    public Boolean deleteDiary(Integer userSeq, Integer diarySeq) {
        Diary diary = diaryRepository.getDiaryByDiarySeq(diarySeq).orElse(null);
        if (diary == null || !userSeq.equals(diary.getUserSeq())) {
            // 해당 하루기록 존재하지 않거나, 사용자의 하루기록이 아닌 경우(권한 없음)
            return false;
        }
        diaryRepository.delete(diary);
        return true;
    }

    @Override
    public Boolean createDiary(Integer userSeq, DiaryCreatePostReq request, String uuidFilename) {
        try {
            Diary diary = new Diary();
            diary.setUserSeq(userSeq);
            diary.setDiaryDate(LocalDate.parse(request.getDiaryInfo().getDate(), DateTimeFormatter.ISO_DATE));
            diary.setDiaryContent(request.getDiaryInfo().getContent());
            diary.setDiaryImg(uuidFilename);
            diaryRepository.save(diary);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
