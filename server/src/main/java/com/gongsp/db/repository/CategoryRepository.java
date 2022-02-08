package com.gongsp.db.repository;

import com.gongsp.db.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<List<Category>> findAllByCategorySeqLessThanEqual(int number);
    Optional<List<Category>> findAllByCategorySeqGreaterThan(int number);
}
