package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.controller.FilmController.validateFilm;

public class FilmControllerTest {

    @Test
    public void testWithBlankName() {
        Film testFilm = new Film(1," ", "Описание фильма",
                LocalDate.of(1928, 12, 1), 30);

        ValidationException e = assertThrows(ValidationException.class, () -> validateFilm(testFilm));
        assertEquals("Название фильма не может быть пустым.", e.getMessage());
    }

    @Test
    public void testWithVeryLongDescription() {
        Film testFilm = new Film(1,"Фильм с очень длинным описанием", "Описание фильма очень длинное" +
                "даже длиннее самого длинного описания фильма с самым длинным описанием и еще длиннее" +
                "описания фильма с самым длинным описанием и содержит слишком много символов , больше двухсот",
                LocalDate.of(1928, 12, 1), 30);

        ValidationException e = assertThrows(ValidationException.class, () -> validateFilm(testFilm));
        assertEquals("Максимальная длина описания — 200 символов.", e.getMessage());
    }

    @Test
    public void testWithVeryOldReleaseDate() {
        Film testFilm = new Film(1,"Фильм очень старый", "Описание фильма",
                LocalDate.of(1525, 5, 5), 30);

        ValidationException e = assertThrows(ValidationException.class, () -> validateFilm(testFilm));
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года!", e.getMessage());
    }

    @Test
    public void testWithNegativeDuration() {
        Film testFilm = new Film(1,"Фильм ", "Описание фильма",
                LocalDate.of(1913, 5, 5), -60);

        ValidationException e = assertThrows(ValidationException.class, () -> validateFilm(testFilm));
        assertEquals("Продолжительность фильма должна быть положительной!", e.getMessage());
    }
}
