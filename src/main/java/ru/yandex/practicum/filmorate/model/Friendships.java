package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendships {
    private int id;
    private int userId;
    private int filmId;
}
