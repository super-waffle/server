package com.gongsp.api.response.user.my_study;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyStudyListGetRes  extends BaseResponseBody {

    private int totalPage;
    private int currentPage;
    private int size;
    private List<StudyRes> studyList;

    public static MyStudyListGetRes of(Integer statusCode, String message,
                                       int totalPage,
                                       int currentPage,
                                       int size,
                                       List<StudyRes> studyList) {
        MyStudyListGetRes result = new MyStudyListGetRes();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        result.setStudyList(studyList);
        result.setTotalPage(totalPage);
        result.setCurrentPage(currentPage);
        result.setSize(size);

        return result;
    }
}