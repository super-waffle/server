package com.gongsp.api.controller;

import com.gongsp.api.response.category.CategoryGetRes;
import com.gongsp.api.service.CategoryService;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/favorite")
    public ResponseEntity<? extends BaseResponseBody> getFavoritesCategories() {
        Optional<List<Category>> list = categoryService.getFavoritesCategories();

        if (!list.isPresent())
            return ResponseEntity.ok(CategoryGetRes.of(404, "No Category List", null));
        return ResponseEntity.ok(CategoryGetRes.of(200, "Success", list.get()));
    }

    @GetMapping("/report")
    public ResponseEntity<? extends BaseResponseBody> getReportsCategories() {
        Optional<List<Category>> list = categoryService.getReportCategories();

        if (!list.isPresent())
            return ResponseEntity.ok(CategoryGetRes.of(404, "No Category List", null));
        return ResponseEntity.ok(CategoryGetRes.of(200, "Success", list.get()));
    }
}
