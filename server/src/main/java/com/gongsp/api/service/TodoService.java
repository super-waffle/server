package com.gongsp.api.service;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.api.request.todo.TodoUpdatePatchReq;
import com.gongsp.db.entity.Todo;

import java.time.LocalDate;
import java.util.List;


public interface TodoService {
    Boolean createTodo(Integer userSeq, TodoCreatePostReq todoInfo);
    List<Todo> getTodoList(Integer userSeq, LocalDate date);
    Boolean deleteTodo(Integer userSeq, Integer todoSeq);
    Boolean updateTodo(Integer userSeq, Integer todoSeq, TodoUpdatePatchReq updateInfo);
}
