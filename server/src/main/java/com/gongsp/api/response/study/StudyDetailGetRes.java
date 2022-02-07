package com.gongsp.api.response.study;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.StudyDay;
import com.gongsp.db.entity.StudyRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudyDetailGetRes extends BaseResponseBody {
    private String hostNickName;
    private String categoryName;
    private String studyTitle;
    private String studyShortDesc;
    private String studyDesc;
    private Integer studyHeadcount;
    private LocalDate studyRecruitEnd;
    private List<Day> day;

//    static class Day {
//        private Integer dayNumber;
//        private LocalTime timeStart;
//        private LocalTime timeEnd;
//    }

    public static StudyDetailGetRes of(Integer statusCode, String message, StudyRoom studyRoom, Integer studyHeadcount, StudyDay[] studyDays) {
        StudyDetailGetRes res = new StudyDetailGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.hostNickName = studyRoom.getHost().getUserNickname();
        res.categoryName = studyRoom.getCategory().getCategoryName();
        res.studyTitle = studyRoom.getStudyTitle();
        res.studyShortDesc = studyRoom.getStudyShortDesc();
        res.studyDesc = studyRoom.getStudyDesc();
        res.studyHeadcount = studyHeadcount;
        res.studyRecruitEnd = studyRoom.getStudyRecruitEnd();
        List<Day> dayList = new ArrayList<>();
        for (StudyDay studyDay : studyDays) {
            Day day = new Day();
            day.setDayNumber(studyDay.getDayNumber());
            day.setTimeStart(studyDay.getStartTime());
            day.setTimeEnd(studyDay.getEndTime());
            dayList.add(day);
        }
        res.day = dayList;
        return res;
    }
}
