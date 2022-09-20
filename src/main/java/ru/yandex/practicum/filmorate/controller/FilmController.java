package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film saveFilm(@RequestBody Film film) throws ValidationException {
        return filmService.saveFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws NotFoundException {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) throws NotFoundException {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws NotFoundException{
        return filmService.addLike(filmService.getFilmById(filmId), userService.getUserById(userId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws NotFoundException{
        return filmService.deleteLike(filmService.getFilmById(filmId), userService.getUserById(userId));
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer countFilms)
            throws NotFoundException, IncorrectParameterException {
        return filmService.getPopularFilms(countFilms);
    }
}

