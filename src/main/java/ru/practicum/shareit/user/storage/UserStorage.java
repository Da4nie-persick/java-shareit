package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(Integer id, User user);

    List<User> allUser();

    User getUserId(Integer id);

    void deleteUser(Integer id);
}
