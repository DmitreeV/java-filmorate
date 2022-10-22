package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public User addFriend(int userId, int friendId) {
        User user = userDao.getUserById(userId);
        User friend = userDao.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info("Пользователь " + user.getName() + " добавлен в список друзей " + friend.getName());
        return user;
    }

    public User deleteFriend(int userId, int friendId) {
        User user = userDao.getUserById(userId);
        User friend = userDao.getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info("Пользователь " + user.getName() + " удален из списка друзей " + friend.getName());
        return user;
    }

    public List<User> getFriends(int userId) {
        User user = userDao.getUserById(userId);
        List<User> friendsList = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            friendsList.add(userDao.getUserById(id));
        }
        log.info("Список друзей пользователя " + user.getName());
        return friendsList;
    }

    public List<User> corporateFriends(int userId, int friendId) {
        User user = userDao.getUserById(userId);
        User friend = userDao.getUserById(friendId);
        List<User> mutualFriends = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                User mutualFriend = userDao.getUserById((id));
                mutualFriends.add(mutualFriend);
            }
        }
        log.info("Список общих друзей пользователей");
        return mutualFriends;
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
