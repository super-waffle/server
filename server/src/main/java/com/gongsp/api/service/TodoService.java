package com.gongsp.api.service;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.api.request.todo.TodoUpdatePatchReq;
import com.gongsp.db.entity.Todo;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;


public interface TodoService {
    Boolean createTodo(Authentication authentication, TodoCreatePostReq todoInfo);
    List<Todo> getTodoList(Authentication authentication, LocalDate date);
    Boolean deleteTodo(Authentication authentication, Integer todoSeq);
    Boolean updateTodo(Authentication authentication, Integer todoSeq, TodoUpdatePatchReq updateInfo);
}