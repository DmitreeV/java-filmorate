package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    List<Film> getAllFilms();

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    int deleteFilm(int id);

    Film getFilmById(int id);

    List<Film> getPopularFilms(int count);
}
