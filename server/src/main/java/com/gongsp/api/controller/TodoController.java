package com.gongsp.api.controller;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.api.request.todo.TodoUpdatePatchReq;
import com.gongsp.api.response.todo.TodoListGetRes;
import com.gongsp.api.service.TodoService;
import com.gongsp.common.model.response.BaseResponseBody;
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
    private TodoService todoService;

    // 투두 항목 추가
    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> createTodo(Authentication authentication, @RequestBody TodoCreatePostReq todoInfo) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }

        if (todoService.createTodo(Integer.parseInt((String) authentication.getPrincipal()), todoInfo)) {
            return ResponseEntity.ok(BaseResponseBody.of(201, "Todo Created"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(400, "Failed to create Todo"));
    }

    // 투두 항목 수정 및 완료버튼 토글
    @PatchMapping("/{todoSeq}")
    public ResponseEntity<? extends BaseResponseBody> updateTodo(Authentication authentication, @PathVariable Integer todoSeq, @RequestBody TodoUpdatePatchReq updateInfo) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Boolean updated = todoService.updateTodo(Integer.parseInt((String) authentication.getPrincipal()), todoSeq, updateInfo);

        if (updated) {
            return ResponseEntity.ok(BaseResponseBody.of(201, "Todo Updated"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(409, "Failed to update Todo"));
    }

    // 투두 항목 삭제
    @DeleteMapping("/{todoSeq}")
    public ResponseEntity<? extends BaseResponseBody> deleteTodo(Authentication authentication, @PathVariable Integer todoSeq) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Boolean deleted = todoService.deleteTodo(Integer.parseInt((String) authentication.getPrincipal()), todoSeq);

        if (deleted) {
            return ResponseEntity.ok(BaseResponseBody.of(204, "Todo Deleted"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(409, "Failed to delete Todo"));
    }

   // 투두리스트 조회
    @GetMapping()
    public ResponseEntity<TodoListGetRes> todoList(Authentication authentication, @RequestParam String date) {
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        LocalDate todoDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        // 투두 목록
        List<Todo> todoList = todoService.getTodoList(userSeq, todoDate);
        // 전체 항목 개수
        Integer totalTodoCount = todoList.size();
        // 완료된 항목 개수
        Integer completedTodoCount = todoService.getCompletedTodoCount(userSeq, todoDate);

        if (todoList.isEmpty()) {
            return ResponseEntity.ok(TodoListGetRes.of(204, "No Content", null, 0, 0));
        }
        return ResponseEntity.ok(TodoListGetRes.of(200, "Success", todoList, totalTodoCount, completedTodoCount));
    }
}
