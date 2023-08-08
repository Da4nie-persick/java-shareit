package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserMapperTest {
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "user1", "email@yandex.ru");
        userDto = new UserDto(1, "userDto", "userDto@gmail.com");
    }

    @Test
    public void toUserDtoTest() {
        UserDto test = UserMapper.toUserDto(user);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getName(), equalTo("user1"));
        assertThat(test.getEmail(), equalTo("email@yandex.ru"));
    }

    @Test
    public void toUserTest() {
        User test = UserMapper.toUser(userDto);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getName(), equalTo("userDto"));
        assertThat(test.getEmail(), equalTo("userDto@gmail.com"));
    }
}