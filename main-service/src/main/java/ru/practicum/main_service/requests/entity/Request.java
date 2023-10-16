package ru.practicum.main_service.requests.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main_service.events.entity.Event;
import ru.practicum.main_service.users.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.main_service.util.Constants.PATTERN_FOR_DATETIME;

@Entity
@Data
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    @JsonFormat(pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime created;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
