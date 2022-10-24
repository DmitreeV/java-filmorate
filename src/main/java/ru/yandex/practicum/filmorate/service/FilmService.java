package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final static LocalDate OLD_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmDao filmDao, UserDao userDao, GenreDao genreDao, LikeDao likeDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
    }

    public List<Film> getAllFilms() {
        List<Film> list = filmDao.getAllFilms();
        for (Film film: list) {
            film.setGenres(genreDao.getGenresByFilmId(film.getId()));
        }
        return list;
    }

    public Film saveFilm(Film film) {
        validateFilm(film);
        Film savedFilm = filmDao.saveFilm(film);
        if (film.getGenres() != null) {
            genreDao.filmGenreUpdate(savedFilm.getId(), film.getGenres());
        }
        savedFilm.setGenres(genreDao.getGenresByFilmId(savedFilm.getId()));
        return savedFilm;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        Film updatedFilm = filmDao.updateFilm(film);
        genreDao.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            genreDao.filmGenreUpdate(film.getId(), film.getGenres());
        }
        updatedFilm.setGenres(genreDao.getGenresByFilmId(film.getId()));
        return updatedFilm;
    }

    public Film getFilmById(int id) {
        Film film = filmDao.getFilmById(id);
        film.setGenres(genreDao.getGenresByFilmId(film.getId()));
        return film;
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        film.setRate(film.getRate() + 1);
        likeDao.saveLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        film.setRate(film.getRate() - 1);
        likeDao.removeLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " убрал лайк у фильма " + film.getName());
        return film;
    }

    public List<Film> getPopularFilms(int countFilms) {
        List<Film> films = filmDao.getPopularFilms(countFilms);
        log.info("Список десяти самых популярных фильмов");
        return films.stream()
                .sorted(this::compare)
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f1.getRate() - f0.getRate();
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
