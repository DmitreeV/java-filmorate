package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User saveUser(User user) {
        return userStorage.saveUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addFriend(User user, User friend) {
        if (user.getId() != 0 && friend.getId() != 0) {
            user.getFriends().add(friend.getId());
            friend.getFriends().add(user.getId());
            log.info("Пользователь " + user.getName() + " добавлен в список друзей " + friend.getName());
        }
        return user;
    }

    public User deleteFriend(User user, User friend) {
        if (user.getId() != 0 && friend.getId() != 0) {
            user.getFriends().remove(friend.getId());
            friend.getFriends().remove(user.getId());
            log.info("Пользователь " + user.getName() + " удален из списка друзей " + friend.getName());
        }
        return user;
    }

    public List<User> getFriends(User user) {
        List<User> friendsList = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            friendsList.add(userStorage.getUserById(id));
        }
        log.info("Список друзей пользователя " + user.getName());
        return friendsList;
    }

    public List<User> corporateFriends(User user, User friend) {
        List<User> mutualFriends = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                User mutualFriend = userStorage.getUserById((id));
                mutualFriends.add(mutualFriend);
            }
        }
        log.info("Список общих друзей пользователей");
        return mutualFriends;
    }
}
