package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film saveFilm(Film film) {
        return filmStorage.saveFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(Film film, User user) {
        if (film.getId() != 0) {
            film.getLikes().add(user.getId());
        }
        film.setLikesCounter(film.getLikesCounter() + 1);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike(Film film, User user) {
        if (film.getId() != 0) {
            film.getLikes().remove(user.getId());
        }
        if (film.getLikesCounter() != 0) {
            film.setLikesCounter(film.getLikesCounter() - 1);
        }
        log.info("Пользователь " + user.getName() + " убрал лайк у фильма " + film.getName());
        return film;
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        Collection<Film> films = filmStorage.getAllFilms();
        log.info("Список десяти самых популярных фильмов");
        return films.stream()
                .sorted(this::compare)
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * Integer.compare(f0.getLikes().size(), f1.getLikes().size());
    }
}
