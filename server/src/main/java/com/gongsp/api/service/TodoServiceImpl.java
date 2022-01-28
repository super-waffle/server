package com.gongsp.api.service;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.common.util.JwtTokenUtil;
import com.gongsp.db.entity.Todo;
import com.gongsp.db.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service("todoService")
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Boolean createTodo(Authentication authentication, TodoCreatePostReq todoInfo) {
        try {
            Todo todo = new Todo();
            todo.setUserSeq(jwtTokenUtil.getUserSeqFromAuth(authentication));
            todo.setTodoDate(todoInfo.getDate());
            todo.setTodoContent(todoInfo.getContent());
            todoRepository.save(todo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Todo> getTodoList(Authentication authentication, LocalDate date) {
        List<Todo> todoList = todoRepository.getTodosByUserSeqAndTodoDate(jwtTokenUtil.getUserSeqFromAuth(authentication), date);
        return todoList;
    }


}
