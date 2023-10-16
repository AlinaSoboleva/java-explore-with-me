package ru.practicum.main_service.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.users.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where ((:users is null or e.initiator.id IN :users) " +
            "and (:categories is null or e.category.id IN :categories) " +
            "and (:states is null or e.state IN :states) " +
            "and (e.eventDate BETWEEN :rangeStart and :rangeEnd))" +
            "order by e.id")
    List<Event> getEventsByParameters(List<Long> users,
                                      List<State> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Pageable page);

    Set<Event> findAllByIdIn(List<Long> ids);

    Set<Event> findAllByCompilation_Id(Long compId);

    List<Event> findByInitiator(User initiator, Pageable pageable);

    @Query("select e from Event as e " +
            "where (upper(e.description) like upper('%' || :text || '%')  or upper(e.annotation) like upper('%' || :text || '%')) " +
            "and (:categories is null OR e.category.id in (:categories)) " +
            "and (:paid is null OR e.paid = :paid) " +
            "and e.state = 'PUBLISHED' " +
            "and (e.eventDate BETWEEN :rangeStart and :rangeEnd) " +
            "and e.participationLimit >" +
            "      (select count(*) " +
            "       from Request as r" +
            "       where r.event.id = e.id " +
            "           and r.status = 'CONFIRMED') " +
            "order by e.eventDate")
    List<Event> getEventsByParametersUnauthorizedAvailable(String text,
                                                           List<Long> categories,
                                                           Boolean paid,
                                                           LocalDateTime rangeStart,
                                                           LocalDateTime rangeEnd,
                                                           Pageable pageable
    );

    Event findEventByIdAndState(Long id, State state);

    @Query("select e from Event as e " +
            "where (:text is null or (upper(e.description) like upper( '%' || :text || '%')) " +
            "or upper(e.annotation) like upper( '%' || :text || '%')) " +
            "and (:categories is null OR e.category.id in (:categories)) " +
            "and (:paid is null OR e.paid = :paid) " +
            "and e.state = 'PUBLISHED' " +
            "and (e.eventDate BETWEEN :rangeStart and :rangeEnd)" +
            "order by e.eventDate")
    List<Event> getEventsByParametersUnauthorizedAll(String text,
                                                     List<Long> categories,
                                                     Boolean paid,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Pageable pageable);
}
