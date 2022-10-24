package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;

@Component
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int saveLike(int filmId, int userId) {
        String qs = "INSERT INTO likes_list (film_id, user_id) VALUES (?, ?)";
        updateRate(filmId);
        return jdbcTemplate.update(qs, filmId, userId);
    }

    @Override
    public int removeLike(int filmId, int userId) {
        String qs = "DELETE FROM likes_list WHERE user_id = ? AND film_id = ?";
        updateRate(filmId);
        return jdbcTemplate.update(qs, filmId, userId);
    }

    private void updateRate(int filmId) {
        jdbcTemplate.update("UPDATE films f " +
                "SET rate = (" +
                "SELECT COUNT(ll.user_id) " +
                "FROM likes_list ll " +
                "WHERE ll.film_id = f.film_id" +
                ") " +
                "WHERE film_id = ?", filmId);
    }
}
