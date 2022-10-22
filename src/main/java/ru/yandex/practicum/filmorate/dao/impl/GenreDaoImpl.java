package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(int id) {
        String qs = "SELECT id, name FROM genres WHERE id = ?";
            return jdbcTemplate.queryForObject(qs, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAll() {
        String qs = "SELECT id, name FROM genres";
        return jdbcTemplate.query(qs, this::mapRowToGenre);
    }

    @Override
    public List<Genre> getForFilm(int filmId) {
        String qs = "SELECT id, name FROM genres WHERE id IN " +
                "(SELECT genres_id FROM films_genres WHERE film_id = ?)";
        return jdbcTemplate.query(qs, this::mapRowToGenre, filmId);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
