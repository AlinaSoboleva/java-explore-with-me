package ru.practicum.main_service.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.entity.Compilation;
import ru.practicum.main_service.compilations.mapper.CompilationMapper;
import ru.practicum.main_service.compilations.repository.CompilationRepository;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.provider.GetEntityProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    private final GetEntityProvider getEntityProvider;

    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> findAllCompilation(boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, page);
        return compilations.stream().map(compilationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = getEntityProvider.getCompilation(compId);
        Set<Event> events = eventRepository.findAllByCompilation_Id(compId);
        compilation.setEvents(events);
        return compilationMapper.toDto(compilation);
    }
}
