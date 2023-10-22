package ru.practicum.main_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdatedEventAdminRequest;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.enumerations.RatingSort;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.events.mapper.EventMapper;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.MainServerException;
import ru.practicum.main_service.provider.GetEntityProvider;
import ru.practicum.main_service.requests.entity.RequestStatus;
import ru.practicum.main_service.requests.repository.RequestRepository;
import ru.practicum.main_service.statistic.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.main_service.events.enumerations.State.CANCELED;
import static ru.practicum.main_service.events.enumerations.State.PUBLISHED;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminEventServiceImpl implements AdminEventService {

    private final GetEntityProvider getEntityProvider;
    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final StatsService statsService;

    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size, String ratingSort) {

        PageRequest page = PageRequest.of(from, size);

        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = LocalDateTime.now().plusYears(100);

        List<Event> events = eventRepository.getEventsByParameters(users, states, categories, rangeStart, rangeEnd, page);

        List<EventFullDto> eventFullDtos = events.stream().map(eventMapper::toFullDto).collect(Collectors.toList());
        Map<Long, Long> views = statsService.returnMapViewStats(events, rangeStart, rangeEnd);
        eventFullDtos = eventFullDtos.stream()
                .peek(dto -> dto.setConfirmedRequests(
                        requestRepository.countByEventIdAndStatus(dto.getId(), RequestStatus.CONFIRMED)))
                .peek(dto -> dto.setViews(views.getOrDefault(dto.getId(), 0L)))
                .collect(Collectors.toList());

        return sortByRatingSort(ratingSort, eventFullDtos);
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdatedEventAdminRequest updatedEvent) {
        if (updatedEvent.getEventDate() != null && updatedEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new MainServerException(String.format("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации %s", updatedEvent.getEventDate()));
        }
        Event event = getEntityProvider.getEvent(eventId);
        if (updatedEvent.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ConflictException(String.format("Событие должно быть в состоянии ожидания публикации %d", eventId));
            }

            switch (updatedEvent.getStateAction()) {
                case REJECT_EVENT:
                    event.setState(CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new MainServerException("Неудалось обработать событие, так как оно находится в неверном состоянии");
            }
        }
        updateEventValues(event, updatedEvent);
        return eventMapper.toFullDto(eventRepository.save(event));
    }

    private void updateEventValues(Event event, UpdatedEventAdminRequest request) {
        event.setAnnotation(request.getAnnotation() == null ? event.getAnnotation() : request.getAnnotation());
        event.setDescription(request.getDescription() == null ? event.getDescription() : request.getDescription());
        event.setEventDate(request.getEventDate() == null ? event.getEventDate() : request.getEventDate());
        event.setLocation(request.getLocation() == null ? event.getLocation() : request.getLocation());
        event.setPaid(request.getPaid() == null ? event.getPaid() : request.getPaid());
        event.setParticipationLimit(request.getParticipantLimit() == null ? event.getParticipationLimit() : request.getParticipantLimit());
        event.setRequestModeration(request.getRequestModeration() == null ? event.getRequestModeration() : request.getRequestModeration());
        event.setTitle(request.getTitle() == null ? event.getTitle() : request.getTitle());
    }

    private List<EventFullDto> sortByRatingSort(String ratingSort, List<EventFullDto> eventFullDtos) {
        if (ratingSort == null) return eventFullDtos;

        switch (RatingSort.valueOf(ratingSort)) {
            case ASC:
                eventFullDtos.sort((e1, e2) -> (int) (e1.getRating() - e2.getRating()));
                break;
            case DESC:
                eventFullDtos.sort((e1, e2) -> (int) (e2.getRating() - e1.getRating()));
                break;
            default:
                throw new MainServerException("Неверное значение параметра ratingSort " + ratingSort);
        }
        return eventFullDtos;
    }
}
