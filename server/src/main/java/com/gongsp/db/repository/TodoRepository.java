package com.gongsp.db.repository;

import com.gongsp.db.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
}
