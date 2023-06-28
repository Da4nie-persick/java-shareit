package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        for (User user : userStorage.allUser()) {
            if (user.getEmail().equals(userDto.getEmail())) {
                throw new ConflictException("Email уже зарегистрирован!");
            }
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || !userDto.getEmail().contains("@")) {
            throw new ValidationException("Не корректный Email!");
        }

        User createUser = userStorage.create(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(createUser);
    }

    @Override
    public UserDto update(UserDto userDto, Integer id) {
        for (User user : userStorage.allUser()) {
            if (user.getEmail().equals(userDto.getEmail()) && !(user.getId().equals(id))) {
                throw new ConflictException("Обновить Email другого пользователя нельзя!");
            }
        }
        User user = UserMapper.toUser(userDto);
        User updateUser = userStorage.update(id, user);
        return UserMapper.toUserDto(updateUser);
    }

    @Override
    public List<UserDto> allUser() {
        return userStorage.allUser().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserId(Integer id) {
        return UserMapper.toUserDto(userStorage.getUserId(id));
    }

    @Override
    public void deleteUser(Integer id) {
        userStorage.deleteUser(id);
    }
}
