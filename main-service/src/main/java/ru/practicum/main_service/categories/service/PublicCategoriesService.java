package ru.practicum.main_service.categories.service;

import ru.practicum.main_service.categories.dto.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> findAll(int from, int size);

    CategoryDto getCategoryById(Long id);
}
