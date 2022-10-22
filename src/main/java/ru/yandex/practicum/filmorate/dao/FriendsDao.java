package ru.yandex.practicum.filmorate.dao;

public interface FriendsDao {

    int saveFriend(int userId, int friendId);

    int removeFriend(int userId, int friendId);
}
