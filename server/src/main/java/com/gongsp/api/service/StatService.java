package com.gongsp.api.service;

import com.gongsp.api.response.stat.DailyStat;
import com.gongsp.api.response.stat.Stat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatService {
    Optional<List<DailyStat>> getUserOneYearStats(int userSeq);
    Optional<Stat> getUserStat(int userSeq, LocalDate date);
}
