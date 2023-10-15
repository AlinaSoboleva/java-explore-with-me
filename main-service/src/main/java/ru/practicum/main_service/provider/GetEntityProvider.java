package ru.practicum.main_service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.entity.Category;
import ru.practicum.main_service.categories.repository.CategoryRepository;
import ru.practicum.main_service.compilations.entity.Compilation;
import ru.practicum.main_service.compilations.repository.CompilationRepository;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.EntityNotFoundException;
import ru.practicum.main_service.requests.entity.Request;
import ru.practicum.main_service.requests.repository.RequestRepository;
import ru.practicum.main_service.users.entity.User;
import ru.practicum.main_service.users.repository.UserRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetEntityProvider {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final CompilationRepository compilationRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    String msg = String.format("Событие с id %d не найдено", eventId);
                    return new EntityNotFoundException(msg);
                });
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Категория с id %d не найдена", categoryId)));
    }

    public Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Подборка с id %d не найдена", compId)));
    }

    public void checkEventCreator(Long userId, Event event) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException(String.format("Событие с id %d не принадлежит пользователю с id %d", event.getId(), userId));
        }
    }

    public Request getRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Запрос с id %d не найден", requestId)));
    }
}
