package com.example.happenhere.service;

import com.example.happenhere.model.CategoryEntity;
import com.example.happenhere.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryEntity saveCategory(String name) {
        CategoryEntity category = new CategoryEntity(name);
        return categoryRepository.save(category);
    }
}
