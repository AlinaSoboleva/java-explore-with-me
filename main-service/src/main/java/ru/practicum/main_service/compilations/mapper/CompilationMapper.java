package ru.practicum.main_service.compilations.mapper;

import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.entity.Compilation;
import ru.practicum.main_service.events.mapper.EventMapper;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toEntity(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false);

        return compilation;
    }

    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(EventMapper::toDto).collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .build();
    }
}
