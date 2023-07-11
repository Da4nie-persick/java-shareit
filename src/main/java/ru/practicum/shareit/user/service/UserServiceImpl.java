package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        User createUser = userStorage.create(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(createUser);
    }

    @Override
    public UserDto update(UserDto userDto, Integer id) {
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
