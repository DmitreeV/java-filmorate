package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {

    private final FilmDao filmDao;
    private final LikeDao likeDao;


    @BeforeEach
    public void saveTestFilms(){

        Film film1 = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        Film newFilm1 = filmDao.saveFilm(film1);

        Film film2 = new Film("Terminal", "test save film",
                LocalDate.of(2004, 10, 14), 124, new Mpa(2, "PG-13"));
        Film newFilm2 = filmDao.saveFilm(film2);

        Film film3 = new Film("Inferno", "test save film",
                LocalDate.of(2016, 10, 8), 121, new Mpa(3, "PG-13"));
        Film newFilm3 = filmDao.saveFilm(film3);
    }

    @Test
    void testSaveFilm() {

        Film film4 = new Film("Cloud Atlas", "test save film",
                LocalDate.of(2012, 9, 8), 172, new Mpa(4, "R"));
        Film newFilm4 = filmDao.saveFilm(film4);

        assertEquals("R", newFilm4.getMpa().getMpaName());
        assertEquals(4, newFilm4.getId());
        assertEquals(4, filmDao.getAllFilms().size());
    }

    @Test
    void testUpdateFilm() {

        Film film = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        film.setId(2);
        filmDao.updateFilm(film);

        assertEquals(film.getId(), filmDao.getFilmById(2).getId());
        assertEquals(film.getName(), filmDao.getFilmById(2).getName());
        assertEquals(film.getDuration(), filmDao.getFilmById(2).getDuration());
    }

    @Test
    void testGetFilmById() {

        Film film1 = filmDao.getFilmById(1);
        Film film2 = filmDao.getFilmById(3);

        assertEquals("Forrest Gump", film1.getName());
        assertEquals("Inferno", film2.getName());
    }

    @Test
    void testGetAllFilms() {

        List<Film> films = filmDao.getAllFilms();

        assertEquals(7, films.size());
    }
}
