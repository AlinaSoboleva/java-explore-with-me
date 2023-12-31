package ru.practicum.main_service.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main_service.compilations.entity.Compilation;
import ru.practicum.main_service.compilations.mapper.CompilationMapper;
import ru.practicum.main_service.compilations.repository.CompilationRepository;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.provider.GetEntityProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    private final GetEntityProvider getEntityProvider;

    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.toEntity(newCompilationDto);
        Set<Event> events = Collections.emptySet();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        }
        compilation.setEvents(events);

        compilation = compilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }

    @Override
    public void deleteComp(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = getEntityProvider.getCompilation(compId);
        compilation.setTitle(request.getTitle() != null ? request.getTitle() : compilation.getTitle());
        Set<Event> events = compilation.getEvents();
        if (request.getEvents() != null) {
            events = eventRepository.findAllByIdIn(List.copyOf(request.getEvents()));
        }
        compilation.setEvents(events);
        for (Event event : events) {
            event.setCompilation(compilation);
        }
        compilation.setPinned(request.getPinned() != null ? request.getPinned() : compilation.getPinned());

        return compilationMapper.toDto(compilation);
    }
}
