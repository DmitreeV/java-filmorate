package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final FriendsDao friendsDao;

    @Autowired
    public UserService(UserDao userDao, FriendsDao friendsDao) {
        this.userDao = userDao;
        this.friendsDao = friendsDao;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User saveUser(User user) {
        validateUser(user);
        return userDao.saveUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        return userDao.updateUser(user);
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь не может быть добавлен.");
        }
        friendsDao.saveFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        friendsDao.removeFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        return userDao.getFriends(userId);
    }

    public List<User> corporateFriends(int userId, int friendId) {
        return userDao.getCorporateFriends(userId, friendId);
    }

    public static void validateUser(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
