package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;


    public FilmDaoImpl(JdbcTemplate jdbcTemplate, MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
    }

    @Override
    public List<Film> getAllFilms() {
        String qs = "SELECT film_id, name, description, release_date, duration, Mpa_rating_id, rate FROM films";
        return jdbcTemplate.query(qs, this::makeFilm);
    }

    @Override
    public Film saveFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", Date.valueOf(film.getReleaseDate()));
        values.put("duration", film.getDuration());
        values.put("Mpa_rating_id", film.getMpa().getId());
        values.put("rate", film.getRate());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String qs = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, Mpa_rating_id = ? WHERE film_id = ?";
        int result = jdbcTemplate.update(qs, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (result != 1) {
            throw new NotFoundException("Фильм не найден.");
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        String qs = "SELECT film_id, name, description, release_date, duration, Mpa_rating_id, rate FROM films " +
                "WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForObject(qs, this::makeFilm, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String qs = "SELECT film_id, name, description, release_date, duration, Mpa_rating_id, rate " +
                "FROM films ORDER BY rate DESC LIMIT ?";
        return jdbcTemplate.query(qs, this::makeFilm, count);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                mpaDao.getById(rs.getInt("Mpa_rating_id")));
        film.setId(rs.getInt("film_id"));
        film.setRate(rs.getInt("rate"));

        return film;
    }
}


