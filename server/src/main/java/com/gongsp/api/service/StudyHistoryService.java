package com.gongsp.api.service;

import java.time.LocalDate;

public interface StudyHistoryService {
    boolean existsMemberToday(Integer userSeq, Integer studySeq, LocalDate now);
    boolean validMemberToday(Integer userSeq, Integer studySeq, LocalDate now);
    // 지각여부를 반환
    boolean createHistory(Integer userSeq, Integer studySeq, boolean isEjected);
    void updateHistoryEjected(Integer userSeq, Integer studySeq, LocalDate curDate, boolean isEjected);
    boolean isMemberLate(Integer userSeq, Integer studySeq, LocalDate curDate);
    boolean existsAnyoneToday(Integer studySeq, LocalDate curDate);
}
