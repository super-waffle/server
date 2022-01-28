package com.gongsp.api.request.todo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoCreatePostReq {
    private LocalDate date;
    private String content;
}
