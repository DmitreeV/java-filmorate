package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTest {

    private final MpaDao mpaDao;

    @Test
    void testGetMpaById() {
        Mpa mpaRating1 = mpaDao.getById(1);
        Mpa mpaRating2 = mpaDao.getById(3);
        Mpa mpaRating3 = mpaDao.getById(5);

        assertEquals("G", mpaRating1.getName());
        assertEquals("PG-13", mpaRating2.getName());
        assertEquals("NC-17", mpaRating3.getName());
    }

    @Test
    void testGetAllMpaRating() {
        List<Mpa> listMpaRating = mpaDao.getAll();

        assertEquals(5, listMpaRating.size());
    }
}
