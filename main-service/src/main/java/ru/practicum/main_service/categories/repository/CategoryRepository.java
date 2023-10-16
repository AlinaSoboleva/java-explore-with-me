package ru.practicum.main_service.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.categories.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
