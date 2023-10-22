package ru.practicum.main_service.events.entity;

import lombok.*;
import ru.practicum.main_service.users.entity.User;

import javax.persistence.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Entity
@Table(name = "likes")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Likes implements Serializable {

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

        private void writeObject(ObjectOutputStream stream)
                throws IOException {
            stream.defaultWriteObject();
        }

        private void readObject(ObjectInputStream stream)
                throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
        }
    }
}
