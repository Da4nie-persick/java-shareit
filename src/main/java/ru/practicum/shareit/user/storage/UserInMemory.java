package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class UserInMemory implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @Override
    public User create(User user) {
        user.setId(++id);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Integer id, User user) {
        User updateUser = userMap.get(id);
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        userMap.put(id, updateUser);
        return updateUser;
    }

    @Override
    public List<User> allUser() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserId(Integer id) {
        return userMap.get(id);
    }

    @Override
    public void deleteUser(Integer id) {
        userMap.remove(id);
    }
}
