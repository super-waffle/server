package com.gongsp.api.service;

import com.gongsp.api.request.diary.DiaryContentInfo;
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

    @Autowired
    private StorageService storageService;

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
        // 기존 이미지 삭제
        String originalDiaryImg = diary.getDiaryImg();
        storageService.delete(originalDiaryImg);
        return true;
    }

    @Override
    public Boolean createDiary(Integer userSeq, DiaryCreatePostReq request, String uuidFilename) {
        try {
            Diary diary = new Diary();
            diary.setUserSeq(userSeq);
            diary.setDiaryDate(LocalDate.parse(request.getDateInfo().getDate(), DateTimeFormatter.ISO_DATE));
            diary.setDiaryContent(request.getContentInfo().getContent());
            diary.setDiaryImg(uuidFilename);
            diaryRepository.save(diary);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateDiary(Integer userSeq, Integer diarySeq, DiaryContentInfo contentInfo, String uuidFilename) {
        Diary diary = diaryRepository.getDiaryByDiarySeq(diarySeq).orElse(null);
        String originalDiaryImg = diary.getDiaryImg();
        if (diary == null || !userSeq.equals(diary.getUserSeq())) {
            // 해당 하루기록 존재하지 않거나, 사용자의 하루기록이 아닌 경우(권한 없음)
            return false;
        }
        try {
            diary.setDiaryContent(contentInfo.getContent());
            diary.setDiaryImg(uuidFilename);
            diaryRepository.save(diary);
            // 기존 이미지 삭제
            storageService.delete(originalDiaryImg);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean existsDiary(Integer diarySeq) {
        return diaryRepository.existsDiaryByDiarySeq(diarySeq);
    }
}
