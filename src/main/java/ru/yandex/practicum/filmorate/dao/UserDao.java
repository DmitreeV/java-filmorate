package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    int deleteUser(int id);

    User getUserById(int id);

    List<User> getFriends(int userId);

    List<User> getCorporateFriends(int userId, int friendId);
}
