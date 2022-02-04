package com.gongsp.api.response.diary;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Diary;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryReadGetRes extends BaseResponseBody {
    private Diary diary;

    public static com.gongsp.api.response.diary.DiaryReadGetRes
    of(Integer statusCode, String message, Diary diary) {
        com.gongsp.api.response.diary.DiaryReadGetRes res = new com.gongsp.api.response.diary.DiaryReadGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setDiary(diary);
        return res;
    }
}
