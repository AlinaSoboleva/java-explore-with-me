package ru.practicum.main_service.events.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.categories.entity.Category;
import ru.practicum.main_service.categories.mapper.CategoryMapper;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.dto.UpdateEventUserRequest;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.entity.Location;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.users.mapper.UserMapper;
import ru.practicum.main_service.validations.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.main_service.util.Constants.PATTERN_FOR_DATETIME;

@Component
public class EventMapper {

    @Autowired
    private static Validator validator;

    public static Event toEntity(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocation(newEventDto.getLocation());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setState(State.PENDING);
        event.setRequestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration());
        event.setPaid(newEventDto.getPaid() != null && newEventDto.getPaid());
        event.setParticipationLimit(newEventDto.getParticipantLimit() == null ? 0L : newEventDto.getParticipantLimit());

        return event;
    }

    public static EventShortDto toDto(Event event) {
        if (event == null) return null;

        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(PATTERN_FOR_DATETIME)))
                .paid(event.getPaid())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .build();
    }

    public static EventFullDto toFullDto(Event event) {
        if (event == null) return null;

        return EventFullDto.builder()
                .id(event.getId())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .annotation(event.getAnnotation())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .title(event.getTitle())
                .state(event.getState() == null ? State.PENDING : event.getState())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .participantLimit(event.getParticipationLimit())
                .paid(event.getPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .build();
    }

    public static Event toUpdatedEntity(UpdateEventUserRequest request, Event event) {
        updateAnnotation(event, request.getAnnotation());
        updateCategory(event, request.getCategory());
        updateDescription(event, request.getDescription());
        updateEventDate(event, request.getEventDate());
        updateLocation(event, request.getLocation());
        updatePaid(event, request.getPaid());
        updateParticipationLimit(event, request.getParticipantLimit());
        updateRequestModeration(event, request.getRequestModeration());
        updateTitle(event, request.getTitle());
        return event;
    }

    private static void updateAnnotation(Event event, String newAnnotation) {
        if (newAnnotation != null) {
            event.setAnnotation(newAnnotation);
        }
    }

    private static void updateCategory(Event event, Long categoryId) {
        if (categoryId != null) {
            Category category = validator.getCategory(categoryId);
            event.setCategory(category);
        }
    }

    private static void updateDescription(Event event, String newDescription) {
        if (newDescription != null) {
            event.setDescription(newDescription);
        }
    }

    private static void updateEventDate(Event event, LocalDateTime newDate) {
        if (newDate != null) {
            event.setEventDate(newDate);
        }
    }

    private static void updateLocation(Event event, Location location) {
        if (location != null) {
            event.setLocation(Location.builder()
                    .lat(location.getLat())
                    .lon(location.getLon())
                    .build());
        }
    }

    private static void updatePaid(Event event, Boolean paid) {
        if (paid != null) {
            event.setPaid(paid);
        }
    }

    private static void updateParticipationLimit(Event event, Long participationLimit) {
        if (participationLimit != null) {
            event.setParticipationLimit(participationLimit);
        }
    }

    private static void updateRequestModeration(Event event, Boolean requestModeration) {
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
    }

    private static void updateTitle(Event event, String newTitle) {
        if (newTitle != null) {
            event.setTitle(newTitle);
        }
    }
}
