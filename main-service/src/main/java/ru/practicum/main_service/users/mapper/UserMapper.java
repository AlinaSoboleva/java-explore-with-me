package ru.practicum.main_service.users.mapper;

import ru.practicum.main_service.users.dto.UserDto;
import ru.practicum.main_service.users.dto.UserShortDto;
import ru.practicum.main_service.users.entity.User;

public class UserMapper {
    public static UserShortDto toShortDto(User user) {
        if (user == null) return null;

        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .rating(user.getRating())
                .build();
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .rating(user.getRating())
                .build();
    }


    public static User toEntity(UserDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setName(userDto.getName());
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setRating(0L);

        return user;
    }
}
