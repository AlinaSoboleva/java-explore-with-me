package ru.practicum.main_service.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.users.dto.UserDto;
import ru.practicum.main_service.users.entity.User;
import ru.practicum.main_service.users.mapper.UserMapper;
import ru.practicum.main_service.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    public UserDto saveUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toEntity(userDto));
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        List<User> users = userRepository.findAllById(ids, page);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
