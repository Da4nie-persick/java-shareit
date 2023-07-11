package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class UserInMemory implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();
    private final Set<String> emailSet = new LinkedHashSet<>();
    private int id = 0;

    @Override
    public User create(User user) {
        if (emailSet.contains(user.getEmail())) {
            throw new ConflictException("Email уже зарегистрирован!");
        }
        user.setId(++id);
        emailSet.add(user.getEmail());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Integer id, User user) {
        User updateUser = userMap.get(id);
        if (emailSet.contains(user.getEmail()) && !((updateUser.getEmail()).equals(user.getEmail()))) {
            throw new ConflictException("Email уже зарегистрирован!");
        }
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            emailSet.remove(updateUser.getEmail());
            emailSet.add(user.getEmail());
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
        emailSet.remove(getUserId(id).getEmail());
        userMap.remove(id);
    }
}
