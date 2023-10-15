package ru.practicum.main_service.compilations.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.entity.Compilation;
import ru.practicum.main_service.events.mapper.EventMapper;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;
    public  Compilation toEntity(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false);

        return compilation;
    }

    public  CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(eventMapper::toDto).collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .build();
    }
}
