package com.gongsp.api.response.todo;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Todo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TodoListGetRes extends BaseResponseBody {
    private List<Todo> todoList;

    public static com.gongsp.api.response.todo.TodoListGetRes of(Integer statusCode, String message, List<Todo> todoList) {
        com.gongsp.api.response.todo.TodoListGetRes res = new com.gongsp.api.response.todo.TodoListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setTodoList(todoList);
        return res;
    }
}
