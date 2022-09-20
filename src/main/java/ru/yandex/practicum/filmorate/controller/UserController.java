package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User saveUser(@RequestBody User user) throws ValidationException {
        return userService.saveUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws NotFoundException {
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId)
            throws NotFoundException {
        return userService.addFriend(userService.getUserById(userId), userService.getUserById(friendId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId)
            throws NotFoundException {
        return userService.deleteFriend(userService.getUserById(userId), userService.getUserById(friendId));
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId)
            throws NotFoundException {
        return userService.getFriends(userService.getUserById(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCorporateFriends(@PathVariable int userId, @PathVariable int otherId)
            throws NotFoundException {
        return userService.corporateFriends(userService.getUserById(userId), userService.getUserById(otherId));
    }
}
