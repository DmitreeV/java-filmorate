package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreDao genreDao;
    private final static LocalDate OLD_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmDao filmDao, UserDao userDao, GenreDao genreDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.genreDao = genreDao;
    }

    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    public Film saveFilm(Film film) {
        validateFilm(film);
        return filmDao.saveFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        return filmDao.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmDao.getFilmById(id);
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        film.addLike(userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        film.deleteLike(userId);
        log.info("Пользователь " + user.getName() + " убрал лайк у фильма " + film.getName());
        return film;
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        Collection<Film> films = filmDao.getAllFilms();
        log.info("Список десяти самых популярных фильмов");
        return films.stream()
                .sorted(this::compare)
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * Integer.compare(f0.getLikes().size(), f1.getLikes().size());
    }

    public static void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(OLD_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
    }
}
