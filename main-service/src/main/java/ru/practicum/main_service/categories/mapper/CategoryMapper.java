package ru.practicum.main_service.categories.mapper;

import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.entity.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) return null;

        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        return category;
    }

    public static CategoryDto toDto(Category category) {
        if (category == null) return null;

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
