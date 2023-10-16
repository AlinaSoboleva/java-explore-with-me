package ru.practicum.main_service.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.requests.entity.Request;
import ru.practicum.main_service.requests.entity.RequestStatus;
import ru.practicum.main_service.users.entity.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester_Id(Long userId);

    List<Request> findAllByEvent_Id(Long eventId);

    Boolean existsByRequesterAndEvent(User user, Event event);

    List<Request> findAllByIdInAndEvent_InitiatorAndEvent_Id(List<Long> requestIds, User user, Long eventId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus requestStatus);
}
