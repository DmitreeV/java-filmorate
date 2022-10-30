package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    //конструктор для тестов UserControllerTest
    public User(int id, String email, String login, String name, LocalDate birthdate) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthdate;
    }

    //конструктор для тестов UserDaoTest
    public User(String email, String login, String name, LocalDate birthdate) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthdate;
    }

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }
}

