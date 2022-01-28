package com.gongsp.api.controller;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.api.service.TodoService;
import com.gongsp.common.model.response.BaseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    TodoService todoService;

    // 투두 항목 추가
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> createTodo(Authentication authentication, @RequestBody TodoCreatePostReq todoInfo) {
        if (todoService.createTodo(authentication, todoInfo)) {
            return ResponseEntity.ok(BaseResponseBody.of(201, "Todo Created"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(400, "Failed to create Todo"));
    }
}
