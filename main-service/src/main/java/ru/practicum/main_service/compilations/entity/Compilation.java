package ru.practicum.main_service.compilations.entity;

import lombok.*;
import ru.practicum.main_service.events.entity.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
@Entity
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compilation")
    private Set<Event> events = new HashSet<>();

    private Boolean pinned;

}
