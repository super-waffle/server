package com.gongsp.api.service;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import org.springframework.security.core.Authentication;


public interface TodoService {
    Boolean createTodo(Authentication authentication, TodoCreatePostReq todoInfo);
}
