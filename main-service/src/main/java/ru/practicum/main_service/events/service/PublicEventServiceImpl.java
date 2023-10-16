package ru.practicum.main_service.events.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.enumerations.Sort;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.events.mapper.EventMapper;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exception.EntityNotFoundException;
import ru.practicum.main_service.exception.MainServerException;
import ru.practicum.main_service.requests.entity.RequestStatus;
import ru.practicum.main_service.requests.repository.RequestRepository;
import ru.practicum.main_service.statistic.StatsService;
import ru.practicum.statsdto.StatRequestDto;
import ru.practicum.statsdto.StatResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.main_service.util.Constants.FORMATTER_FOR_DATETIME;

@Service
@RequiredArgsConstructor
@Transactional
@PropertySource("classpath:application.properties")
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final StatsService statsService;

    private final EventMapper eventMapper;

    @Value("${app.name}")
    private String appName;

    public List<EventShortDto> getAllEvents(String text,
                                            List<Long> categories,
                                            Boolean paid,
                                            LocalDateTime start,
                                            LocalDateTime end,
                                            boolean onlyAvailable,
                                            Sort sort,
                                            int from,
                                            int size, HttpServletRequest request) {
        checkDates(start, end);
        StatRequestDto statRequestDto = new StatRequestDto();
        statRequestDto.setApp(appName);
        statRequestDto.setUri(request.getRequestURI());
        statRequestDto.setIp(request.getRemoteAddr());
        statRequestDto.setTimestamp(LocalDateTime.now().format(FORMATTER_FOR_DATETIME));
        statsService.saveStats(statRequestDto);

        if (start == null) start = LocalDateTime.now();
        if (end == null) end = LocalDateTime.now().plusYears(100);
        if (end.isBefore(start)) {
            throw new MainServerException("Конец события не может быть раньше начала");
        }

        Pageable page = PageRequest.of(from, size);
        List<Event> events;
        if (onlyAvailable) {
            events = eventRepository.getEventsByParametersUnauthorizedAvailable(text, categories, paid, start, end, page);
        } else {
            events = eventRepository.getEventsByParametersUnauthorizedAll(text, categories, paid, start, end, page);
        }

        if (events != null) {
            for (Event event : events) {
                statRequestDto.setUri("/events/" + event.getId());
                statRequestDto.setTimestamp(LocalDateTime.now().format(FORMATTER_FOR_DATETIME));
                statsService.saveStats(statRequestDto);
            }
        }
        Map<Long, Long> views = statsService.returnMapViewStats(events, start, end);
        List<EventShortDto> eventShortDtos = events.stream().map(eventMapper::toDto).collect(Collectors.toList());

        eventShortDtos.stream()
                .peek(dto -> dto.setConfirmedRequests(
                        requestRepository.countByEventIdAndStatus(dto.getId(), RequestStatus.CONFIRMED)))
                .peek(dto -> dto.setViews(views.getOrDefault(dto.getId(), 0L)))
                .collect(Collectors.toList());


        if (sort.equals(Sort.VIEWS)) {
            eventShortDtos.sort((e1, e2) -> (int) (e2.getViews() - e1.getViews()));
        }
        return eventShortDtos;
    }

    @Override
    public EventFullDto findEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findEventByIdAndState(id, State.PUBLISHED);
        if (event == null) {
            throw new EntityNotFoundException("Событие должно быть опубликованно");
        }
        StatRequestDto statRequestDto = new StatRequestDto();
        statRequestDto.setApp(appName);
        statRequestDto.setIp(request.getRemoteAddr());
        statRequestDto.setUri(request.getRequestURI());
        statRequestDto.setTimestamp(LocalDateTime.now().format(FORMATTER_FOR_DATETIME));

        statsService.saveStats(statRequestDto);

        List<String> uris = List.of("/events/" + event.getId());
        List<StatResponseDto> viewStats;
        try {
            viewStats = statsService.getAllStats(uris, LocalDateTime.now().minusYears(100).format(FORMATTER_FOR_DATETIME),
                    LocalDateTime.now().plusYears(100).format(FORMATTER_FOR_DATETIME));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        EventFullDto eventFullDto = eventMapper.toFullDto(event);
        eventFullDto.setConfirmedRequests(requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED));
        eventFullDto.setViews(viewStats.isEmpty() ? 0L : viewStats.get(0).getHits());

        return eventFullDto;
    }

    private void checkDates(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && (start.isAfter(LocalDateTime.now()) || start.isAfter(end))) {
            throw new MainServerException(String.format("Неверные параметны даты, start : %s, end: %s", start, end));
        }
    }
}
