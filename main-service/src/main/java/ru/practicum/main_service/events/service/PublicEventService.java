package ru.practicum.main_service.events.service;

import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.enumerations.Sort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;


public interface PublicEventService {
    List<EventShortDto> getAllEvents(String text,
                                     List<Long> categories,
                                     Boolean paid,
                                     LocalDateTime start,
                                     LocalDateTime end,
                                     boolean onlyAvailable,
                                     Sort sort,
                                     int from,
                                     int size, HttpServletRequest request, String ratingSort);


    EventFullDto findEventById(Long id, HttpServletRequest request);
}
