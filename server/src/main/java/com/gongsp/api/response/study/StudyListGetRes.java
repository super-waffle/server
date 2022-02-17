package com.gongsp.api.response.study;

import com.gongsp.api.request.study.StudyParameter;
import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyListGetRes extends BaseResponseBody {
    private int totalPage;
    private int currentPage;
    private int type;
    private String key;
    private List<StudyRes> data;

    public static StudyListGetRes of(Integer statusCode, String message, StudyParameter studyParameter, int totalCount, List<StudyRes> data) {
        StudyListGetRes res = new StudyListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        int totalPage = totalCount / 20;
        if (totalCount % 20 != 0)
            totalPage += 1;
        res.setTotalPage(totalPage);
        int currentPage = studyParameter.getPage();
        if (currentPage == 0)
            currentPage = 1;
        res.setCurrentPage(currentPage);
        res.setType(studyParameter.getType());
        res.setKey(studyParameter.getKey());
        res.setData(data);
        return res;
    }
}
