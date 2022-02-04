package com.gongsp.api.response.study;

import com.gongsp.api.response.user.my_study.StudyRes;
import com.gongsp.common.model.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyDetailInfoGetRes extends BaseResponseBody {
    StudyRes study;

    static public StudyDetailInfoGetRes of (int statusCode, String message, StudyRes study) {
        StudyDetailInfoGetRes result = new StudyDetailInfoGetRes();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        result.setStudy(study);
        return result;
    }
}
