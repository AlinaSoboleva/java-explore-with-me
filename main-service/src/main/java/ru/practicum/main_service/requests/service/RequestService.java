package ru.practicum.main_service.requests.service;

import ru.practicum.main_service.requests.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> findAllByRequester(Long userId);

    RequestDto create(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
