package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Test
    void createUserEmailTest() {
        User user = new User();
        user.setEmail("");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.now().minusYears(16));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.create(user);
        });
        assertEquals("Email не может быть пустым", trow.getMessage());
    }

    @Test
    void createUserEmptyLoginTest() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("");
        user.setName("Name");
        user.setBirthday(LocalDate.now().minusYears(16));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.create(user);
        });
        assertEquals("Логин не может быть пустым", trow.getMessage());
    }

    @Test
    void createUserEmptyNameReplaceLoginTest() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("Login");
        user.setName("");
        user.setBirthday(LocalDate.now().minusYears(16));
/*
        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.create(user);
        });*/
        userController.create(user);
        assertEquals(user.getName(), userController.getById(1).getName());
    }


    @Test
    void createUserFailBirthdayTest() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.now().plusDays(1));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.create(user);
        });
        assertEquals("Дата рождения не может быть в будущем", trow.getMessage());
    }
}