package ru.practicum.main_service.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.EntityNotFoundException;
import ru.practicum.main_service.exception.MainServerException;
import ru.practicum.main_service.requests.dto.RequestDto;
import ru.practicum.main_service.requests.entity.Request;
import ru.practicum.main_service.requests.entity.RequestStatus;
import ru.practicum.main_service.requests.mapper.RequestMapper;
import ru.practicum.main_service.requests.repository.RequestRepository;
import ru.practicum.main_service.users.entity.User;
import ru.practicum.main_service.validations.Validator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;
    private final Validator validator;

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> findAllByRequester(Long userId) {
        validator.getUser(userId);
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        if (requests.isEmpty()) {
            return Collections.emptyList();
        }
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        validator.getUser(userId);
        Request request = validator.getRequest(requestId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("Можно отменить только свой запрос на участие, " +
                    "userId = %s, requestId = %s.", userId, requestId));
        }
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        return RequestMapper.toDto(request);
    }

    @Override
    @Transactional
    public RequestDto create(Long userId, Long eventId) {
        User user = validator.getUser(userId);
        Event event = validator.getEvent(eventId);

        if (event.getInitiator().equals(user)) {
            throw new ConflictException("Пользователь является создателем события");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие должно быть опубликованно");
        }
        if (event.getParticipationLimit() > 0 && event.getParticipationLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("Превышен лимит участников");
        }
        if (requestRepository.existsByRequesterAndEvent(user, event)) {
            throw new ConflictException("Пользователь уже отправил запрос для данного события");
        }

        Request request = transientRequest(user, event);
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        try {
            request = requestRepository.save(request);
            return RequestMapper.toDto(request);
        } catch (DataIntegrityViolationException ex) {
            throw new MainServerException(ex.getMessage());
        }
    }

    private Request transientRequest(User user, Event event) {
        RequestStatus requestStatus;
        if (!event.getRequestModeration() || event.getParticipationLimit() == 0) {
            requestStatus = RequestStatus.CONFIRMED;
        } else {
            requestStatus = RequestStatus.PENDING;
        }

        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(requestStatus);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
