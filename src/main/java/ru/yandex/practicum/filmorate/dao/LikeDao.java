package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface LikeDao {

    void saveLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Integer> getLike(int filmId);
}
