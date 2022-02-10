package com.gongsp.api.service;

import com.gongsp.api.response.stat.DailyStat;
import com.gongsp.api.response.stat.Stat;
import com.gongsp.db.entity.LogTime;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.LogTimeRepository;
import com.gongsp.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "statService")
public class StatServiceImpl implements StatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogTimeRepository logTimeRepository;

    @Override
    public Optional<List<DailyStat>> getUserOneYearStats(int userSeq) {
        Optional<List<LogTime>> times = logTimeRepository.selectAllOneYearTimesOfUser(userSeq, LocalDate.now());
        if (!times.isPresent())
            return Optional.empty();

        List<DailyStat> result = new ArrayList<>();

        for (LogTime time : times.get()) {
            DailyStat stat = new DailyStat();
            stat.setDate(time.getLogDate());
            stat.setDayTotalTime(time.getLogMeeting() + time.getLogStudy());
            result.add(stat);
        }

        return Optional.of(result);
    }

    @Override
    public Optional<Stat> getUserStat(int userSeq, LocalDate date) {
        Optional<User> userInfo = userRepository.findUserByUserSeq(userSeq);
        Optional<LogTime> logTime = logTimeRepository.findLogTimeByUserSeqAndLogDate(userSeq, date);

        if (!userInfo.isPresent() || !logTime.isPresent())
            return Optional.empty();

        User user = userInfo.get();
        LogTime log = logTime.get();
        Stat stat = new Stat();

        stat.setDate(log.getLogDate());
        stat.setStudyStartTime(log.getLogStartTime());
        stat.setStudyEndTime(log.getLogEndTime());
        stat.setMeetingRoomTime(log.getLogMeeting());
        stat.setStudyRoomTime(log.getLogStudy());
        stat.setDayTotalStudyTime(log.getLogMeeting() + log.getLogStudy());
        stat.setUserTotalStudyTime(user.getUserTimeTotal());

        return Optional.of(stat);
    }
}
