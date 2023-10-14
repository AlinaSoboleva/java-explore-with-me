package ru.practicum.main_service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.entity.Category;
import ru.practicum.main_service.categories.mapper.CategoryMapper;
import ru.practicum.main_service.categories.repository.CategoryRepository;
import ru.practicum.main_service.validations.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCategoriesServiceImpl implements PublicCategoriesService {

    private final CategoryRepository repository;

    private final Validator validator;

    @Override
    public List<CategoryDto> findAll(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Category> response = repository.findAll(pageRequest).toList();
        return response
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = validator.getCategory(id);
        return CategoryMapper.toDto(category);
    }
}
