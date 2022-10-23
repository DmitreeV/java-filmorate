package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDaoTest {

    private final UserDao userDao;
    private final FriendsDao friendsDao;

    @BeforeEach
    public void saveTestUsers(){

        User user1 = new User(1,"dmitree@mail.ru", "Dmitree30", "Dmitree" ,
                LocalDate.of(1993, 5, 30));
        User newUser1 = userDao.saveUser(user1);

        User user2 = new User(2,"katrin@mail.ru", "Katrin12", "Katrin",
                LocalDate.of(1985, 6, 21));
        User newUser2 = userDao.saveUser(user2);

        User user3 = new User(3,"elena@mail.ru", "Elena222","Elena",
                LocalDate.of(1976, 11, 2));
        User newUser3 = userDao.saveUser(user3);
    }

    @Test
    void testSaveFilm() {

        User user4 = new User(4,"daniil@mail.ru", "Daniil88", "Daniil",
                LocalDate.of(1988, 8, 14));
        User newUser4 = userDao.saveUser(user4);

        assertEquals("Daniil88", newUser4.getLogin());
        assertEquals(4, newUser4.getId());
        assertEquals(4, userDao.getAllUsers().size());
    }

    @Test
    void testUpdateUser() {

        User user = new User(3,"elena@mail.ru", "Elena222","Elena",
                LocalDate.of(1976, 11, 2));
        user.setBirthday(LocalDate.of(1988, 8, 15));
        user.setEmail("elena@yandex.ru");
        User newUser = userDao.updateUser(user);

        assertEquals("elena@yandex.ru", userDao.getUserById(3).getEmail());
        assertEquals(LocalDate.of(1988, 8, 15), userDao.getUserById(3).getBirthday());
    }

    @Test
    void testGetUserById() {

        User user1 = userDao.getUserById(1);
        User user2 = userDao.getUserById(3);

        assertEquals("Dmitree", user1.getName());
        assertEquals("Elena", user2.getName());
    }

    @Test
    void testGetAllUsers() {

        List<User> users = userDao.getAllUsers();

        assertEquals(3, users.size());
    }

    @Test
    void testSaveFriend() {

        int result = friendsDao.saveFriend(1, 2);
        int res = friendsDao.saveFriend(1, 3);

        assertEquals(2, result + res );
    }

    @Test
    void testRemoveFriend() {
        int result = friendsDao.removeFriend(1, 2);

        assertEquals(0, result);
    }

    @Test
    void testGetFriends() {
        List<User> friends = userDao.getFriends(1);

        assertEquals(0, friends.size());
    }

    @Test
    void getCorporateFriends() {
        List<User> friends = userDao.getCorporateFriends(2, 3);

        assertEquals(0, friends.size());
    }
}
