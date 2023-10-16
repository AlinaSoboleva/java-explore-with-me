package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.enumerations.Sort;
import ru.practicum.main_service.events.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main_service.util.Constants.PATTERN_FOR_DATETIME;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {

    private final PublicEventService eventService;

    @GetMapping
    public List<EventShortDto> findAllBySearch(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false, defaultValue = "VIEWS") String sort,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации");
        return eventService
                .getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, Sort.valueOf(sort), from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto findEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получение события с id {}", id);
        return eventService.findEventById(id, request);
    }


}
