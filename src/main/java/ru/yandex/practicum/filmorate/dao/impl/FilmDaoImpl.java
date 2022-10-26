package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_rating m ON m.MPA_id = f.mpa_rating_id;";
        return jdbcTemplate.query(qs, this::makeFilm);
    }

    @Override
    public Film saveFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", Date.valueOf(film.getReleaseDate()));
        values.put("duration", film.getDuration());
        values.put("mpa_rating_id", film.getMpa().getId());
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
                "duration = ?, mpa_rating_id = ? WHERE film_id = ?";
        int result = jdbcTemplate.update(qs, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (result != 1) {
            throw new NotFoundException("Фильм не найден.");
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_rating m ON m.MPA_id = f.mpa_rating_id " +
                "WHERE f.film_id = ?;";
        try {
            return jdbcTemplate.queryForObject(qs, this::makeFilm, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        final String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_rating m ON m.MPA_id = f.mpa_rating_id " +
                "LEFT OUTER JOIN likes_list ll on f.film_id = ll.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(ll.film_id) " +
                "DESC LIMIT ?;";
        return jdbcTemplate.query(qs, this::makeFilm, count);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("MPA_id"))
                        .name(rs.getString("MPA_name"))
                        .build())
                .rate(rs.getInt("rate"))
                .build();
    }
}


