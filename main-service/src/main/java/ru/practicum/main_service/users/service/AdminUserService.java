package ru.practicum.main_service.users.service;

import ru.practicum.main_service.users.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    UserDto saveUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void deleteUser(long userId);
}
