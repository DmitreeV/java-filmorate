package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(int id) {
        String qs = "SELECT id, name FROM genres WHERE id = ?";
        if (id < 0) {
            throw new NotFoundException("Неверно передан ID Genre.");
        }
        return jdbcTemplate.queryForObject(qs, this::makeGenre, id);
    }

    @Override
    public List<Genre> getAll() {
        String qs = "SELECT id, name FROM genres";
        return jdbcTemplate.query(qs, this::makeGenre);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String qs = "SELECT id, name FROM genres WHERE id IN " +
                "(SELECT id FROM films_genres WHERE film_id = ?)";

        return jdbcTemplate.query(qs, this::makeGenre, filmId);
    }

    public void filmGenreUpdate(Integer filmId, List<Genre> genreList) {
        List<Genre> withoutRepetitions = genreList.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO films_genres (film_id, id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, withoutRepetitions.get(i).getId());
                    }

                    public int getBatchSize() {
                        return withoutRepetitions.size();
                    }
                });
    }

    @Override
    public void deleteGenresByFilmId(int filmId) {
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
