package ru.practicum.main_service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.events.entity.Likes;

public interface LikesRepository extends JpaRepository<Likes, Likes.Key> {

    Likes findLikesByKey(Likes.Key key);

    void deleteLikesByKey(Likes.Key key);
}
