package com.gongsp.api.service;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.api.request.todo.TodoUpdatePatchReq;
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

    @Override
    public Boolean createTodo(Authentication authentication, TodoCreatePostReq todoInfo) {
        try {
            Todo todo = new Todo();
            todo.setUserSeq(Integer.parseInt((String) authentication.getPrincipal()));
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
        List<Todo> todoList = todoRepository.getTodosByUserSeqAndTodoDate(Integer.parseInt((String) authentication.getPrincipal()), date);
        return todoList;
    }

    @Override
    public Boolean deleteTodo(Authentication authentication, Integer todoSeq) {
        Todo todo = todoRepository.getTodoByTodoSeq(todoSeq);
        if (todo == null || authentication == null) {
            return false;
        }
        if (todo.getUserSeq().toString().equals(authentication.getPrincipal())) {
            todoRepository.delete(todo);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateTodo(Authentication authentication, Integer todoSeq, TodoUpdatePatchReq updateInfo) {
        Todo todo = todoRepository.getTodoByTodoSeq(todoSeq);
        if (todo == null || authentication == null) {
            return false;
        }
        if (todo.getUserSeq().toString().equals(authentication.getPrincipal())) {
            todo.setTodoContent(updateInfo.getContent());
            todo.setTodoCompleted(updateInfo.getCompleted());
            todoRepository.save(todo);
            return true;
        }
        return false;
    }


}
