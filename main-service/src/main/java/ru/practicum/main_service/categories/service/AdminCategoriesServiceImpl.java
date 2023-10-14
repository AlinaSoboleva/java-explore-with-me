package ru.practicum.main_service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.entity.Category;
import ru.practicum.main_service.categories.mapper.CategoryMapper;
import ru.practicum.main_service.categories.repository.CategoryRepository;
import ru.practicum.main_service.validations.Validator;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoriesServiceImpl implements AdminCategoriesService {

    private final CategoryRepository categoryRepository;

    private final Validator validator;

    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toEntity(categoryDto));
        return CategoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = validator.getCategory(catId);
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }
}
