package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.events.service.PrivateEventService;
import ru.practicum.main_service.requests.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@Validated
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @PutMapping("/{eventId}/like")
    public EventShortDto putLike(@PathVariable Long eventId,
                                 @PathVariable Long userId) {
        log.info("Получен запрос на лайк от пользователя с id " + userId + " для события с id " + eventId);
        return privateEventService.putLike(eventId, userId, true);
    }

    @PutMapping("/{eventId}/dislike")
    public EventShortDto putDislike(@PathVariable Long eventId,
                                    @PathVariable Long userId) {
        log.info("Получен запрос на дизлайк от пользователя с id " + userId + " для события с id " + eventId);
        return privateEventService.putLike(eventId, userId, false);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size,
                                             @RequestParam(required = false) String ratingSort) {
        log.info("Получение списка событий пользователя с id {}", userId);
        return privateEventService.findAll(userId, from, size, ratingSort);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение события с id {} пользователем с id {}", eventId, userId);
        return privateEventService.getUserEvent(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Добавление нового события пользователем {}", newEventDto);
        EventFullDto eventFullDto = privateEventService.addEvent(userId, newEventDto);
        log.info("Пользователь с id {} добавли новое событие {}", userId, eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                        @Valid @RequestBody EventRequestStatusUpdateRequest requestUpdateStatusDto) {
        log.info("Изменение статуса заявок на участие в событии пользоваетля с id {}", userId);
        return privateEventService.updateRequest(userId, eventId, requestUpdateStatusDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest eventUserRequest) {
        log.info("Изменение события с id {} пользователем с id {}", eventId, userId);
        return privateEventService.update(userId, eventId, eventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsInfo(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Получение пользователем с id {} информации о запросах" +
                " на участие в событии с id {}", userId, eventId);
        return privateEventService.getRequestsInfo(userId, eventId);
    }

}
