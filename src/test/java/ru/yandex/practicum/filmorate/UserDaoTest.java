package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDaoTest {

    private final UserDao userDao;
    private final FriendsDao friendsDao;

    @Test
    void testSaveFilm() {

        User testUser = new User("daniil@mail.ru", "Daniil88", "Daniil",
                LocalDate.of(1988, 8, 14));
        userDao.saveUser(testUser);

        assertEquals("Daniil88", testUser.getLogin());
        assertEquals(1, testUser.getId());
        assertEquals(1, userDao.getAllUsers().size());
    }

    @Test
    void testUpdateUser() {

        User testUser = new User("elena@mail.ru", "Elena222", "Elena",
                LocalDate.of(1976, 11, 2));
        userDao.saveUser(testUser);
        testUser.setBirthday(LocalDate.of(1988, 8, 15));
        testUser.setEmail("elena@yandex.ru");
        userDao.updateUser(testUser);

        assertEquals("elena@yandex.ru", userDao.getUserById(1).getEmail());
        assertEquals(LocalDate.of(1988, 8, 15), userDao.getUserById(1).getBirthday());
    }

    @Test
    void testGetUserById() {

        User user1 = new User("dmitree@mail.ru", "Dmitree30", "Dmitree",
                LocalDate.of(1993, 5, 30));
        userDao.saveUser(user1);

        User user2 = new User("katrin@mail.ru", "Katrin12", "Katrin",
                LocalDate.of(1985, 6, 21));
        userDao.saveUser(user2);

        assertEquals("Dmitree", user1.getName());
        assertEquals("Katrin", user2.getName());
    }

    @Test
    void testGetAllUsers() {

        User user1 = new User("dmitree@mail.ru", "Dmitree30", "Dmitree",
                LocalDate.of(1993, 5, 30));
        userDao.saveUser(user1);

        User user2 = new User("katrin@mail.ru", "Katrin12", "Katrin",
                LocalDate.of(1985, 6, 21));
        userDao.saveUser(user2);

        List<User> users = userDao.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void testSaveFriend() {

        User user1 = new User("dmitree@mail.ru", "Dmitree30", "Dmitree",
                LocalDate.of(1993, 5, 30));
        userDao.saveUser(user1);

        User user2 = new User("katrin@mail.ru", "Katrin12", "Katrin",
                LocalDate.of(1985, 6, 21));
        userDao.saveUser(user2);

        friendsDao.saveFriend(1, 2);

        List<User> friends = userDao.getFriends(user1.getId());

        assertEquals(friends.size(), 1);
    }

    @Test
    void testRemoveFriend() {

        User user1 = new User("dmitree@mail.ru", "Dmitree30", "Dmitree",
                LocalDate.of(1993, 5, 30));
        userDao.saveUser(user1);

        User user2 = new User("katrin@mail.ru", "Katrin12", "Katrin",
                LocalDate.of(1985, 6, 21));
        userDao.saveUser(user2);

        friendsDao.saveFriend(1, 2);

        friendsDao.removeFriend(1, 2);

        List<User> friends = userDao.getFriends(user1.getId());

        assertEquals(friends.size(), 0);
    }

    @Test
    void testGetFriends() {
        User user1 = new User("dmitree@mail.ru", "Dmitree30", "Dmitree",
                LocalDate.of(1993, 5, 30));
        userDao.saveUser(user1);

        User user2 = new User("katrin@mail.ru", "Katrin12", "Katrin",
                LocalDate.of(1985, 6, 21));
        userDao.saveUser(user2);

        friendsDao.saveFriend(1, 2);

        List<User> friends = userDao.getFriends(user1.getId());

        assertEquals(friends.size(), 1);
        assertEquals(friends.get(0), user2);
    }

    @Test
    public void testGetCorporateFriends() {
        User user1 = new User("dmitree@mail.ru", "Dmitree30", "Dmitree",
                LocalDate.of(1993, 5, 30));
        userDao.saveUser(user1);

        User user2 = new User("katrin@mail.ru", "Katrin12", "Katrin",
                LocalDate.of(1985, 6, 21));
        userDao.saveUser(user2);

        User testUser = new User("daniil@mail.ru", "Daniil88", "Daniil",
                LocalDate.of(1988, 8, 14));
        userDao.saveUser(testUser);

        User user4 = new User("z@.ru", "NePutin", "Vladimir",
                LocalDate.of(1965, 8, 22));
        userDao.saveUser(user4);

        User user5 = new User("kirill@.ru", "Patriarh", "Kirill",
                LocalDate.of(1933, 8, 21));
        userDao.saveUser(user5);

        User user6 = new User("user6@.ru", "six6", "Aleksandr",
                LocalDate.of(1933, 8, 21));
        userDao.saveUser(user6);

        friendsDao.saveFriend(user1.getId(), testUser.getId());
        friendsDao.saveFriend(user2.getId(), testUser.getId());
        friendsDao.saveFriend(user4.getId(), testUser.getId());
        friendsDao.saveFriend(user5.getId(), testUser.getId());
        friendsDao.saveFriend(user6.getId(), testUser.getId());

        friendsDao.saveFriend(user2.getId(), user1.getId());
        friendsDao.saveFriend(user4.getId(), user1.getId());
        friendsDao.saveFriend(user5.getId(), user1.getId());
        friendsDao.saveFriend(user6.getId(), user1.getId());
        friendsDao.saveFriend(testUser.getId(), user1.getId());

        friendsDao.saveFriend(user2.getId(), user4.getId());
        friendsDao.saveFriend(user4.getId(), user2.getId());
        friendsDao.saveFriend(user5.getId(), user2.getId());
        friendsDao.saveFriend(user6.getId(), user2.getId());
        friendsDao.saveFriend(testUser.getId(), user4.getId());

        List<User> userCommonFriends1 = userDao.getCorporateFriends(user4.getId(), user5.getId());
        System.out.println(userCommonFriends1);
        assertEquals(userCommonFriends1.get(0), user1);
        assertEquals(userCommonFriends1.get(1), user2);
        assertEquals(userCommonFriends1.get(2), testUser);

        List<User> userCommonFriends = userDao.getCorporateFriends(user4.getId(), user6.getId());
        System.out.println(userCommonFriends);
        assertEquals(userCommonFriends1.get(0), user1);
        assertEquals(userCommonFriends1.get(1), user2);
        assertEquals(userCommonFriends1.get(2), testUser);

        List<User> userCommonFriends2 = userDao.getCorporateFriends(user1.getId(), user2.getId());
        System.out.println(userCommonFriends2);
        assertEquals(userCommonFriends2.get(0).getName(), testUser.getName());

        List<User> userCommonFriends3 = userDao.getCorporateFriends(user2.getId(), testUser.getId());
        System.out.println(userCommonFriends3);
        assertEquals(userCommonFriends3.get(0).getName(), user1.getName());
        assertEquals(userCommonFriends3.get(1).getName(), user4.getName());
    }
}
