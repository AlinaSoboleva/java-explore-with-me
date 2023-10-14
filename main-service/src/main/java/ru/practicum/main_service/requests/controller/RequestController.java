package ru.practicum.main_service.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.requests.dto.RequestDto;
import ru.practicum.main_service.requests.service.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> findAllByUser(@PathVariable Long userId) {
        log.info("Получение запросов пользователя с id {}", userId);
        return requestService.findAllByRequester(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Создание запроса к событию с id {} пользователем с id {}", eventId, userId);
        return requestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Отмена запроса с id {} пользователем с id {}", requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }
}
