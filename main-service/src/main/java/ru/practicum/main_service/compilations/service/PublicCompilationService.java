package ru.practicum.main_service.compilations.service;

import ru.practicum.main_service.compilations.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> findAllCompilation(boolean pinned, int from, int size);

    CompilationDto getById(Long compId);
}
