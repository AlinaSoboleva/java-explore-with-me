package ru.practicum.main_service.events.service;

import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.requests.dto.RequestDto;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> findAll(Long userId, int from, int size);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest requestUpdateStatusDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest);

    EventFullDto getUserEvent(Long userId, Long eventId);

    List<RequestDto> getRequestsInfo(Long userId, Long eventId);
}
