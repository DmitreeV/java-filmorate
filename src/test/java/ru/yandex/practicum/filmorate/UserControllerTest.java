package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage.validateUser;

public class UserControllerTest {

    @Test
    public void testWithBlankEmail() {
        User testUser = new User(1," ", "Login", "Name",
                LocalDate.of(1988, 9, 1));
        User testUser2 = new User(2,"dmitree.yandex.ru ", "Login", "Name",
                LocalDate.of(1988, 9, 1));

        ValidationException e = assertThrows(ValidationException.class, () -> validateUser(testUser));
        ValidationException ex = assertThrows(ValidationException.class, () -> validateUser(testUser2));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", ex.getMessage());
    }

    @Test
    public void testWithBlankLogin() {
        User testUser = new User(1,"dmitree@yandex.ru ", " ", "Name",
                LocalDate.of(1988, 9, 1));

        ValidationException e = assertThrows(ValidationException.class, () -> validateUser(testUser));
        assertEquals("Логин не может быть пустым и содержать пробелы.", e.getMessage());
    }

    @Test
    public void testWithBlankName() {
        User testUser = new User(1,"dmitree@yandex.ru ", "Login", "",
                LocalDate.of(1988, 9, 1));
        validateUser(testUser);

        assertEquals(testUser.getLogin(), testUser.getName());
    }

    @Test
    public void testWithNotCorrectBirthdate() {
        User testUser = new User(1,"dmitree@yandex.ru ", "Login", "Name",
                LocalDate.of(2023, 1, 1));

        ValidationException e = assertThrows(ValidationException.class, () -> validateUser(testUser));
        assertEquals("Дата рождения не может быть в будущем.", e.getMessage());
    }
}

