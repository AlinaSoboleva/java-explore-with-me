package ru.practicum.main_service.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.users.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u " +
            "where (:ids is null OR u.id IN :ids)" +
            "order by u.id")
    List<User> findAllById(List<Long> ids, Pageable page);
}
