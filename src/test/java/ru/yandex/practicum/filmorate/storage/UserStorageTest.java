package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserStorageTest {
    private final UserStorage userStorage;

    private final User user1 = User.builder()
            .email("1@ya.ru")
            .login("login1")
            .name("Name1")
            .birthday(LocalDate.parse("1967-03-01"))
            .build();

    private final User user2 = User.builder()
            .email("2@ya.ru")
            .login("login2")
            .name("Name2")
            .birthday(LocalDate.parse("1967-03-01"))
            .build();

    private final User user3 = User.builder()
            .email("3ya.ru")
            .login("login3")
            .name("Name3")
            .birthday(LocalDate.parse("1967-03-01"))
            .build();

    @Test
    public void createUserTest() {
        userStorage.save(user1);
        AssertionsForClassTypes.assertThat(user1).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user1).extracting("name").isNotNull();
    }

    @Test
    public void getAllUsersTest() {
        userStorage.save(user2);
        userStorage.save(user3);
        Collection<User> dbUsers = userStorage.findAll();
        assertEquals(2, dbUsers.size());
    }

    @Test
    public void deleteUserTest() {
        Collection<User> beforeDelete = userStorage.findAll();
        userStorage.deleteById(1);
        Collection<User> afterDelete = userStorage.findAll();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}