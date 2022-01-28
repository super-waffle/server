package com.gongsp.db.repository;

import com.gongsp.db.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> getTodosByUserSeqAndTodoDate(Integer userSeq, LocalDate date);
    Todo getTodoByTodoSeq(Integer todoSeq);
}
