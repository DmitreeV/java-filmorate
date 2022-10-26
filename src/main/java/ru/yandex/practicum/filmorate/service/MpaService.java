package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {

    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getById(int id) {
        if (id < 0 ) {
            throw new NotFoundException("Неверно передан ID MPA.");
        }
        log.info("Получен рейтинг MPA с идентификатором " + id + ".");
        return mpaDao.getById(id);
    }

    public List<Mpa> getAll() {
        log.info("Получен список всех рейтингов MPA.");
        return mpaDao.getAll();
    }
}
