package com.gongsp.api.service;

import com.gongsp.db.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<List<Category>> getFavoritesCategories();
    Optional<List<Category>> getReportCategories();
}
