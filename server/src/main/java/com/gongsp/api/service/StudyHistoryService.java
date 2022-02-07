package com.gongsp.api.service;

import java.time.LocalDate;

public interface StudyHistoryService {
    boolean existsMemberToday(Integer userSeq, Integer studySeq, LocalDate now);
    boolean validMemberToday(Integer userSeq, Integer studySeq, LocalDate now);
    void createHistory(Integer userSeq, Integer studySeq);
}
