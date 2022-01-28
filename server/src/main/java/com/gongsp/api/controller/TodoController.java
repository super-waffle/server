package com.gongsp.api.controller;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.api.response.todo.TodoListGetRes;
import com.gongsp.api.service.TodoService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.common.util.JwtTokenUtil;
import com.gongsp.db.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    TodoService todoService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    // 투두 항목 추가
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> createTodo(Authentication authentication, @RequestBody TodoCreatePostReq todoInfo) {
        if (todoService.createTodo(authentication, todoInfo)) {
            return ResponseEntity.ok(BaseResponseBody.of(201, "Todo Created"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(400, "Failed to create Todo"));
    }

    // 투두리스트 조회
    @GetMapping()
    public ResponseEntity<TodoListGetRes> todoList(Authentication authentication, @RequestParam String date) {
        List<Todo> todoList = todoService.getTodoList(authentication, LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
        if (todoList.isEmpty()) {
            return ResponseEntity.ok(TodoListGetRes.of(204, "No Content", null));
        }
        return ResponseEntity.ok(TodoListGetRes.of(200, "Success", todoList));
    }
}
