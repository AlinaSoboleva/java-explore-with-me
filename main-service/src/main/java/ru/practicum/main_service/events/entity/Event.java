package ru.practicum.main_service.events.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.main_service.categories.entity.Category;
import ru.practicum.main_service.compilations.entity.Compilation;
import ru.practicum.main_service.events.enumerations.State;
import ru.practicum.main_service.users.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    private String description;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator")
    private User initiator;
    @Embedded
    private Location location;
    private Boolean paid;
    @Column(name = "participation_limit")
    private Long participationLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(name = "confirmed_requests", nullable = false)
    private Long confirmedRequests;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;
    @Column(name = "title", nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id")
    @EqualsAndHashCode.Exclude
    private Compilation compilation;

    private Long rating;

}
