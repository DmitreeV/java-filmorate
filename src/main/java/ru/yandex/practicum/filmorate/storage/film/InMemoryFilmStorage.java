package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public abstract class InMemoryFilmStorage implements FilmDao {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private int generateId() {
        return id++;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film saveFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм сохранен.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден.");
        }
        films.put(film.getId(), film);
        log.info("Фильм с номером " + film.getId() + " обновлен.");
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм не найден!");
        }
    }
}
