package ru.practicum.main_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.entity.Category;
import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.events.mapper.EventMapper;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.MainServerException;
import ru.practicum.main_service.provider.GetEntityProvider;
import ru.practicum.main_service.requests.dto.RequestDto;
import ru.practicum.main_service.requests.entity.Request;
import ru.practicum.main_service.requests.entity.RequestStatus;
import ru.practicum.main_service.requests.mapper.RequestMapper;
import ru.practicum.main_service.requests.repository.RequestRepository;
import ru.practicum.main_service.users.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;
    private final GetEntityProvider getEntityProvider;

    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findAll(Long userId, int from, int size) {
        User user = getEntityProvider.getUser(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findByInitiator(user, pageable).stream().map(eventMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = getEntityProvider.getUser(userId);
        Category category = getEntityProvider.getCategory(Long.valueOf(newEventDto.getCategory()));
        Event event = eventMapper.toEntity(newEventDto);
        event.setCategory(category);
        event.setInitiator(user);
        event.setConfirmedRequests(0L);
        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsInfo(Long userId, Long eventId) {
        getEntityProvider.getUser(userId);
        Event event = getEntityProvider.getEvent(eventId);
        getEntityProvider.checkEventCreator(userId, event);
        List<Request> requests = requestRepository.findAllByEvent_Id(eventId);
        return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        getEntityProvider.getUser(userId);
        Event event = getEntityProvider.getEvent(eventId);
        getEntityProvider.checkEventCreator(userId, event);

        return eventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest) {
        Event event = getEntityProvider.getEvent(eventId);
        getEntityProvider.getUser(userId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        eventMapper.toUpdatedEntity(eventUserRequest, event);
        if (eventUserRequest.getStateAction() != null) {
            switch (eventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                default:
                    throw new RuntimeException("Неверное значение state action:  " + eventUserRequest.getStateAction());
            }
        }
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest requestUpdateStatusDto) {
        Event event = getEntityProvider.getEvent(eventId);
        User user = getEntityProvider.getUser(userId);
        getEntityProvider.checkEventCreator(userId, event);

        if (!event.getRequestModeration() || event.getParticipationLimit() == 0) {
            throw new ConflictException("Лимит заявок = 0 или отключенна пре-модерация");
        }


        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataIntegrityViolationException("Данное событые недоступно для изменения," +
                    " так как до события осталось меньше 2х часов");
        }

        List<Request> requests = requestRepository.findAllByIdInAndEvent_InitiatorAndEvent_Id(requestUpdateStatusDto.getRequestIds(), user, eventId);

        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (event.getParticipationLimit() != 0 && event.getParticipationLimit() <= confirmedRequests) {
            throw new ConflictException("Нельзя подтвердить заявку, так как достигнут лимит по заявкам " +
                    "на событие, userId = " + userId + ", eventId = " + eventId +
                    ", eventRequestStatusUpdateRequest: " + requestUpdateStatusDto);
        }
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        for (Request request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new MainServerException("Нельзя отменить уже принятую заявку на участие, userId = " + userId
                        + ", eventId = " + eventId + ", eventRequestStatusUpdateRequest: "
                        + requestUpdateStatusDto);
            }
            if (!request.getEvent().getId().equals(eventId)) {
                rejected.add(request);
                continue;
            }

            switch (requestUpdateStatusDto.getStatus()) {
                case CONFIRMED:
                    if (confirmedRequests < event.getParticipationLimit()) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        confirmedRequests++;
                        confirmed.add(request);
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        rejected.add(request);
                    }
                    break;

                case REJECTED:
                    request.setStatus(RequestStatus.REJECTED);
                    rejected.add(request);
                    break;
            }
        }

        event.setConfirmedRequests(confirmedRequests);
        //   eventRepository.save(event);
        //   requestRepository.saveAll(requests);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmed.stream().map(RequestMapper::toDto).collect(Collectors.toList()));
        result.setRejectedRequests(rejected.stream().map(RequestMapper::toDto).collect(Collectors.toList()));

        return result;
    }
}
