package com.gongsp.api.response.schedule;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class ScheduleListGetRes extends BaseResponseBody {
    private Map map;

    public static com.gongsp.api.response.schedule.ScheduleListGetRes of(Integer statusCode, String message, Map map) {
        com.gongsp.api.response.schedule.ScheduleListGetRes res = new com.gongsp.api.response.schedule.ScheduleListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setMap(map);
        return res;
    }
}
