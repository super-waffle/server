package com.gongsp.api.response.user.my_study;

import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyStudyListGetRes  extends BaseResponseBody {

    private PagedStudyResult studyResult;

    public static MyStudyListGetRes of(Integer statusCode, String message, PagedStudyResult studyResult) {
        MyStudyListGetRes result = new MyStudyListGetRes();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        result.setStudyResult(studyResult);

        return result;
    }
}