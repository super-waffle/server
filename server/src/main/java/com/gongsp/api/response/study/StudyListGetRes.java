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

    public static StudyListGetRes of(Integer statusCode, String message, StudyParameter studyParameter, int totalPage, List<StudyRes> data) {
        StudyListGetRes res = new StudyListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setTotalPage(totalPage);
        res.setCurrentPage(studyParameter.getPage());
        res.setType(studyParameter.getType());
        res.setKey(studyParameter.getKey());
        res.setData(data);
        return res;
    }
}
