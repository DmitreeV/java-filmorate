package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;

@Component
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int saveFriend(int userId, int friendId) {
        String qs = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
        return jdbcTemplate.update(qs, userId, friendId);
    }

    @Override
    public int removeFriend(int userId, int friendId) {
        String qs = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(qs, userId, friendId);
    }
}
