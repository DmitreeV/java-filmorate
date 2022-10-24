package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public abstract class InMemoryUserStorage implements UserDao {
    private final Map<Integer, User> users = new HashMap<>();

    private int id = 1;

    private int generateId() {
        return id++;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User saveUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь сохранен.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден.");
        }
        users.put(user.getId(), user);
        log.info("Данные пользователя с ID " + user.getId() + " обновлены.");
        return user;
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }
}