package com.gongsp.api.service;

import com.gongsp.db.entity.Category;
import com.gongsp.db.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(value = "categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<List<Category>> getFavoritesCategories() {
        return categoryRepository.findAllByCategorySeqLessThanEqual(100);
    }

    @Override
    public Optional<List<Category>> getReportCategories() {
        return categoryRepository.findAllByCategorySeqGreaterThan(100);
    }
}
