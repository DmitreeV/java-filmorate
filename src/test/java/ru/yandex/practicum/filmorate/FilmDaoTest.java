package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDaoTest {

    private final FilmDao filmDao;
    private final LikeDao likeDao;
    private final UserDao userDao;

    @Test
    void testSaveFilm() {

        Film film = new Film("Cloud Atlas", "test save film",
                LocalDate.of(2012, 9, 8), 172, new Mpa(4, "R"));
        filmDao.saveFilm(film);

        assertEquals("R", film.getMpa().getName());
        assertEquals(1, film.getId());
        assertEquals(1, filmDao.getAllFilms().size());

        System.out.println(film);
    }

    @Test
    void testUpdateFilm() {

        Film film = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        filmDao.saveFilm(film);
        film.setName("Brat");
        film.setDuration(120);
        filmDao.updateFilm(film);

        assertEquals("Brat", filmDao.getFilmById(1).getName());
        assertEquals(120, filmDao.getFilmById(1).getDuration());
    }

    @Test
    void testGetFilmById() {

        Film film1 = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        filmDao.saveFilm(film1);
        Film film2 = new Film("Inferno", "test save film",
                LocalDate.of(2016, 10, 8), 121, new Mpa(3, "PG-13"));
        filmDao.saveFilm(film2);

        Film film3 = filmDao.getFilmById(1);
        Film film4 = filmDao.getFilmById(2);

        assertEquals("Forrest Gump", film3.getName());
        assertEquals("Inferno", film4.getName());
    }

    @Test
    void testGetAllFilms() {

        Film film1 = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        filmDao.saveFilm(film1);
        Film film2 = new Film("Inferno", "test save film",
                LocalDate.of(2016, 10, 8), 121, new Mpa(3, "PG-13"));
        filmDao.saveFilm(film2);

        List<Film> films = filmDao.getAllFilms();

        System.out.println(films.get(0));
        System.out.println(films.get(1));

        assertEquals(2, films.size());
    }

    @Test
    void testSaveLike() {

        Film film1 = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        filmDao.saveFilm(film1);
        User testUser = new User("daniil@mail.ru", "Daniil88", "Daniil",
                LocalDate.of(1988, 8, 14));
        userDao.saveUser(testUser);

        likeDao.saveLike(film1.getId(), testUser.getId());

        List<Film> likesFilm = filmDao.getPopularFilms(10);

        assertEquals(likesFilm.size(), 1);
    }

    @Test
    void testRemoveLike() {

        Film film1 = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        filmDao.saveFilm(film1);
        User testUser = new User("daniil@mail.ru", "Daniil88", "Daniil",
                LocalDate.of(1988, 8, 14));
        userDao.saveUser(testUser);

        likeDao.saveLike(film1.getId(), testUser.getId());
        likeDao.removeLike(film1.getId(), testUser.getId());

        List<Integer> likesFilm = likeDao.getLike(film1.getId());

        assertEquals(likesFilm.size(), 0);
    }

    @Test
    public void testGetPopularFilm() {
        Film film = new Film("Forrest Gump", "test save film",
                LocalDate.of(1994, 6, 23), 136, new Mpa(1, "PG-13"));
        filmDao.saveFilm(film);

        Film film2 = new Film("Cloud Atlas", "test save film",
                LocalDate.of(2012, 9, 8), 172, new Mpa(4, "R"));
        filmDao.saveFilm(film2);

        Film film3 = new Film("Inferno", "test save film",
                LocalDate.of(2016, 10, 8), 121, new Mpa(3, "PG-13"));
        filmDao.saveFilm(film3);

        User user = new User("daniil@mail.ru", "Daniil88", "Daniil",
                LocalDate.of(1988, 8, 14));
        userDao.saveUser(user);

        likeDao.saveLike(film.getId(), user.getId());
        likeDao.saveLike(film2.getId(), user.getId());

        List<Film> popularFilm = filmDao.getPopularFilms(2);

        System.out.println(popularFilm.get(0));

        assertEquals(popularFilm.size(), 2);
    }
}
