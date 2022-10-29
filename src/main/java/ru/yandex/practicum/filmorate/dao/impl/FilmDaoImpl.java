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
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl.createGenreByRs;


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
        List<Film> allFilms = jdbcTemplate.query(qs, this::makeFilm);

        addGenres(allFilms);
        addLikes(allFilms);

        return allFilms;
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
    public int deleteFilm(int id) {
        final String qs = "DELETE FROM films WHERE film_id = ?";
        return jdbcTemplate.update(qs, id);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        final String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA_rating m ON m.MPA_id = f.mpa_rating_id " +
                "LEFT OUTER JOIN likes_list ll on f.film_id = ll.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(ll.film_id) " +
                "DESC LIMIT ?;";

        List<Film> popFilm = jdbcTemplate.query(qs, this::makeFilm, count);

        addGenres(popFilm);
        addLikes(popFilm);
        return popFilm;
    }

    private void addGenres(List<Film> films) {
        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM genres g, films_genres fg WHERE fg.id = g.id AND fg.film_id IN (" + filmIds + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.addGenre(createGenreByRs(rs));
        });
    }

    private void addLikes(List<Film> films) {
        String qs = "SELECT * FROM likes_list;";

        Map<Integer, Film> filmById = films.stream()
                .collect(Collectors.toMap(Film::getId, identity()));

        jdbcTemplate.query(qs, (rs) -> {
            Integer filmId = rs.getInt("film_id");
            Integer userId = rs.getInt("user_id");

            Optional.ofNullable(filmById.get(filmId))
                    .ifPresent(film -> film.addLike(userId));
        });
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
                .genres(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
    }
}


