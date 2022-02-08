package com.gongsp.api.response.category;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryGetRes extends BaseResponseBody {
    private List<Category> list;

    public static CategoryGetRes of(int statusCode, String message, List<Category> list) {
        CategoryGetRes result = new CategoryGetRes();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        result.setList(list);
        return result;
    }
}
