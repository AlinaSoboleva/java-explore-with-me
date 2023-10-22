package ru.practicum.main_service.events.service;

import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdatedEventAdminRequest;
import ru.practicum.main_service.events.enumerations.State;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    EventFullDto updateEvent(Long eventId, UpdatedEventAdminRequest updatedEvent);

    List<EventFullDto> getAllEventsByAdmin(
            List<Long> users, List<State> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size, String ratingSort);
}
