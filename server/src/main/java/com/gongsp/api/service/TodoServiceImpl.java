package com.gongsp.api.service;

import com.gongsp.api.request.todo.TodoCreatePostReq;
import com.gongsp.api.request.todo.TodoUpdatePatchReq;
import com.gongsp.db.entity.Todo;
import com.gongsp.db.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service("todoService")
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Boolean createTodo(Integer userSeq, TodoCreatePostReq todoInfo) {
        try {
            Todo todo = new Todo();
            todo.setUserSeq(userSeq);
            todo.setTodoDate(todoInfo.getDate());
            todo.setTodoContent(todoInfo.getContent());
            todoRepository.save(todo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Todo> getTodoList(Integer userSeq, LocalDate date) {
        List<Todo> todoList = todoRepository.getTodosByUserSeqAndTodoDate(userSeq, date);
        return todoList;
    }

    @Override
    public Boolean deleteTodo(Integer userSeq, Integer todoSeq) {
        Todo todo = todoRepository.getTodoByTodoSeq(todoSeq);
        if (todo == null) {
            return false;
        }
        if (todo.getUserSeq().equals(userSeq)) {
            todoRepository.delete(todo);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateTodo(Integer userSeq, Integer todoSeq, TodoUpdatePatchReq updateInfo) {
        Todo todo = todoRepository.getTodoByTodoSeq(todoSeq);
        if (todo == null) {
            return false;
        }
        if (todo.getUserSeq().equals(userSeq)) {
            todo.setTodoContent(updateInfo.getContent());
            todo.setTodoCompleted(updateInfo.getCompleted());
            todoRepository.save(todo);
            return true;
        }
        return false;
    }


}
