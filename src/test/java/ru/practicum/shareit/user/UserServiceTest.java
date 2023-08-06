package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    UserService userService;
    User user;
    User user2;
    User user3;

    @BeforeEach
    public void beforeEach() {
        userService = new UserServiceImpl(userRepository);
        user = new User(1, "user2", "user2@yandex.ru");
        user2 = new User(2, "user3", "user3@gmail.com");
        user3 = new User(3, "us", "user3@gmail.com");
    }

    @Test
    public void createSuccessfulTest() {
        when(userRepository.save(any())).thenReturn(user);
        UserDto createUser = userService.create(UserMapper.toUserDto(user));
        assertThat(createUser.getName(), equalTo(user.getName()));
        assertThat(createUser.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    public void updateSuccessfulTest() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any())).thenReturn(user);
        UserDto updateUser = userService.update(UserMapper.toUserDto(user2), user.getId());
        assertThat(updateUser.getName(), equalTo(user.getName()));
        assertThat(updateUser.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    public void allUserSuccessfulTest() {
        when(userRepository.findAll()).thenReturn(List.of(user, user2, user3));
        List<UserDto> userDtoList = userService.allUser();
        assertThat(UserMapper.toUserDto(user).getName(), equalTo(userDtoList.get(0).getName()));
        assertThat(UserMapper.toUserDto(user2).getName(), equalTo(userDtoList.get(1).getName()));
        assertThat(UserMapper.toUserDto(user3).getName(), equalTo(userDtoList.get(2).getName()));
    }

    @Test
    public void getUserIdSuccessfulTest() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        UserDto userDto = userService.getUserId(1);
        assertThat(UserMapper.toUserDto(user).getName(), equalTo(userDto.getName()));
        assertThat(UserMapper.toUserDto(user).getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void deleteUserSuccessfulTest() {
        userService.deleteUser(any());
        verify(userRepository, times(1)).deleteById(any());
    }

    @Test
    public void getUserIdNoSuchElementExceptionTest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> userService.update(UserMapper.toUserDto(user), 1));
        assertThat(exception.getMessage(), equalTo("Пользователь с данным id не найден!"));
    }
}