package ru.practicum.main_service.compilations.service;

import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request);

    void deleteComp(Long compId);
}
