package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
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
        String qs = "SELECT film_id, name, description, release_date, duration, MPA_id, rate FROM films";
        return jdbcTemplate.query(qs, this::mapRowToFilm);
    }

    @Override
    public Film saveFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", Date.valueOf(film.getReleaseDate()));
        values.put("duration", film.getDuration());
        values.put("MPA_id", film.getMpa().getMpaId());
        values.put("rate", 0);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        return getFilmById(simpleJdbcInsert.executeAndReturnKey(values).intValue());
    }

    @Override
    public Film updateFilm(Film film) {
        String qs = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, MPA_id = ? WHERE film_id = ?";
        jdbcTemplate.update(qs, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getMpaId(), film.getId());

        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        String qs = "SELECT film_id, name, description, release_date, duration, MPA_id, rate FROM films " +
                "WHERE film_id = ?";
            return jdbcTemplate.queryForObject(qs, this::mapRowToFilm, id);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(resultSet.getString("name"), resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(), resultSet.getInt("duration"),
                mpaDao.getById(resultSet.getInt("MPA_id")));
        film.setId(resultSet.getInt("film_id"));
        film.setRate(resultSet.getInt("rate"));

        return film;
    }
}


