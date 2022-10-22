package ru.yandex.practicum.filmorate.dao;

public interface LikeDao {

    int saveLike(int filmId, int userId);

    int removeLike(int filmId, int userId);
}
