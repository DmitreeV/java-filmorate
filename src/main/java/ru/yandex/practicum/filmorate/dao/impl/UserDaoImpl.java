package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String qs = "SELECT user_id, email, login, name, birthday FROM users";
        return jdbcTemplate.query(qs, this::makeUser);
    }

    @Override
    public User saveUser(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        final String qs = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int result = jdbcTemplate.update(qs, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        if (result != 1) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        String qs = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(qs, this::makeUser, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public List<User> getFriends(int userId) {
        String qs = "SELECT user_id, email, login, name, birthday FROM users " +
                "WHERE user_id IN (SELECT friend_id FROM friendships WHERE user_id = ?)";
        return jdbcTemplate.query(qs, this::makeUser, userId);
    }

    @Override
    public List<User> getCorporateFriends(int userId, int friendId) {
        String qs = "SELECT user_id, email, login, name, birthday FROM users " +
                "WHERE user_id IN " +
                "(SELECT friend_id FROM friendships WHERE user_id = ? AND friend_id NOT IN (?, ?) AND " +
                "friend_id IN (SELECT friend_id FROM friendships WHERE user_id = ?))";
        return jdbcTemplate.query(qs, this::makeUser, userId, friendId, userId, friendId);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getString("email"),
                rs.getString("login"),
                rs.getDate("birthday").toLocalDate());
        user.setName(rs.getString("name"));
        user.setId(rs.getInt("user_id"));

        return user;
    }
}
