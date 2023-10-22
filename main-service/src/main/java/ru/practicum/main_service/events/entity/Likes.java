package ru.practicum.main_service.events.entity;

import lombok.*;
import ru.practicum.main_service.users.entity.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "likes")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Likes {

    @EmbeddedId
    private Key key;

    private Boolean positive;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key implements Serializable {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "event_id", nullable = false)
        private Event eventId;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User userId;

    }
}