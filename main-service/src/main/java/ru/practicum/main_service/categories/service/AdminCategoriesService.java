package ru.practicum.main_service.categories.service;

import ru.practicum.main_service.categories.dto.CategoryDto;

public interface AdminCategoriesService {
    CategoryDto saveCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    void deleteCategory(Long catId);
}
