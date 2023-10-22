package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdatedEventAdminRequest;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.events.service.AdminEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main_service.util.Constants.PATTERN_FOR_DATETIME;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Slf4j
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventState(@PathVariable Long eventId,
                                         @RequestBody @Valid UpdatedEventAdminRequest event) {
        EventFullDto eventFullDto = adminEventService.updateEvent(eventId, event);
        log.info("Статус и данные события изменены {}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<State> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeEnd,
                                        @RequestParam(required = false) String ratingSort,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Получение списка событий администратором");
        return adminEventService.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size, ratingSort);
    }
}
