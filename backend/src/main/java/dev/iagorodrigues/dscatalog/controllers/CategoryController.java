package dev.iagorodrigues.dscatalog.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.iagorodrigues.dscatalog.entities.Category;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Books"));
        categories.add(new Category(2L, "Electronics"));
        return ResponseEntity.ok().body(categories);
    }
}
