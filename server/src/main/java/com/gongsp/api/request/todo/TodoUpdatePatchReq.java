package com.gongsp.api.request.todo;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TodoUpdatePatchReq {
    private String content;
    private Boolean completed;
}
