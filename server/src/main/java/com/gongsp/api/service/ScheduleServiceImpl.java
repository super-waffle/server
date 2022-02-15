package com.gongsp.api.service;

import com.gongsp.api.response.schedule.ScheduleRes;
import com.gongsp.api.response.schedule.StudyDailyHistory;
import com.gongsp.db.entity.Study;
import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.entity.StudyHistory;
import com.gongsp.db.entity.StudySchedule;
import com.gongsp.db.repository.StudyDayRepository;
import com.gongsp.db.repository.StudyHistoryRepository;
import com.gongsp.db.repository.StudyRepository;
import com.gongsp.db.repository.StudyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private StudyDayRepository studyDayRepository;

    @Autowired
    private StudyScheduleRepository studyScheduleRepository;
    @Autowired
    private StudyHistoryRepository studyHistoryRepository;

    @Override
    public List<StudySchedule> findAllUserIncludedActiveStudies(Integer userSeq, LocalDate date, Integer day) {
        List<StudySchedule> studySchedules = studyScheduleRepository.findAllByUserSeq(userSeq, date, day);
        if(studySchedules == null)
            return null;
        for (int i = 0; i < studySchedules.size(); i++) {
            Optional<StudyHistory> optionalStudyHistory= studyHistoryRepository.findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, studySchedules.get(i).getStudySeq(), date);
            if(!optionalStudyHistory.isPresent())
                studySchedules.get(i).setIsAttend(2);
            else{
                if(optionalStudyHistory.get().getHistoryLate())
                    studySchedules.get(i).setIsAttend(1);
                else
                    studySchedules.get(i).setIsAttend(0);
            }
        }
        return studySchedules;
    }

    @Override
    public List<ScheduleRes> getAllStudiesInAWeek(int userSeq, LocalDate date) {
        int startDateInterval = 0;
        int endDateInterval = 0;

        String dayOfWeek = date.getDayOfWeek().name();

        switch (dayOfWeek) {
            case "MONDAY":
                endDateInterval = 6;
                break;
            case "TUESDAY":
                startDateInterval = 1;
                endDateInterval = 5;
                break;
            case "WEDNESDAY":
                startDateInterval = 2;
                endDateInterval = 4;
                break;
            case "THURSDAY":
                startDateInterval = 3;
                endDateInterval = 3;
                break;
            case "FRIDAY":
                startDateInterval = 4;
                endDateInterval = 2;
                break;
            case "SATURDAY":
                startDateInterval = 5;
                endDateInterval = 1;
                break;
            case "SUNDAY":
                startDateInterval = 6;
                break;
        }

        Optional<List<Study>> studiesInfo = studyRepository.selectAllStudiesBetweenGivenDates(userSeq, date, startDateInterval, endDateInterval);

        LocalDate[] dates = setLocalDates(date, date.getDayOfWeek().getValue(), startDateInterval, endDateInterval);

        List<ScheduleRes> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            ScheduleRes res = new ScheduleRes();
            res.setDate(dates[i + 1]);
            res.setStudySchedules(new ArrayList<>());
            result.add(res);
        }

        if (!studiesInfo.isPresent()) {
            return result;
        }

        List<Study> studies = studiesInfo.get();

        for (Study study : studies) {
            Optional<StudyDay[]> studyDays = studyDayRepository.findAllByStudySeqOrderByDayNumber(study.getStudySeq());

            if (studyDays.isPresent()) {
                for (StudyDay studyDay : studyDays.get()) {
                    int dayNum = studyDay.getDayNumber();
                    LocalDate resDate = result.get(dayNum - 1).getDate();
                    if (study.getStartDate().isBefore(resDate) || study.getStartDate().isEqual(resDate))
                        result.get(dayNum - 1).getStudySchedules().add(new StudyDailyHistory(study, studyDay));
                }
            }
        }

        for (ScheduleRes res : result) {
            LocalDate resDate = res.getDate();

            for (StudyDailyHistory history : res.getStudySchedules()) {
                if (studyHistoryRepository.existsStudyHistoryByStudySeqAndHistoryDate(history.getStudySeq(), resDate)) {
                    Optional<StudyHistory> dayHistoryInfo = studyHistoryRepository
                            .findStudyHistoryByUserSeqAndStudySeqAndHistoryDate(userSeq, history.getStudySeq(), resDate);

                    if (!dayHistoryInfo.isPresent()) {
                        history.setIsAttend((short) 2);
                        continue;
                    }

                    StudyHistory dayHistory = dayHistoryInfo.get();

                    if (dayHistory.getHistoryEject())
                        history.setIsAttend((short) 3);
                    else if (dayHistory.getHistoryLate())
                        history.setIsAttend((short) 1);
                    else
                        history.setIsAttend((short) 0);

                } else {
                    history.setIsAttend((short) 2);
                }
            }
            res.sortSchedules();
        }

        return result;
    }

    private LocalDate[] setLocalDates(LocalDate date, int value, int startDateInterval, int endDateInterval) {
        LocalDate[] result = new LocalDate[8];

        for (int i = 1; i < value; i++) {
            int diff = value - i;
            result[i] = date.minusDays(diff);
        }

        result[value] = date;

        for (int i = value + 1; i < 8; i++) {
            int diff = i - value;
            result[i] = date.plusDays(diff);
        }

        return result;
    }
}
