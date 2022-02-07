package com.gongsp.api.response.user;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class ApplicantsListGetRes extends BaseResponseBody {
    private Collection<User> applicants;

    public static ApplicantsListGetRes of (int statusCode, String message, Collection<User> applicants) {
        ApplicantsListGetRes result = new ApplicantsListGetRes();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        result.setApplicants(applicants);
        return result;
    }
}
