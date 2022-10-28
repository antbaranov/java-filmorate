package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        check(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен с логином: {}, email: {}", user.getLogin(), user.getEmail());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Невозможно обновить данные, так как пользователя не существует");
        }
        check(user);
        users.put(user.getId(), user);
        log.info("Данные пользователя с id: {}, логином: {} успешно обновлена", user.getId(), user.getLogin());
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    private void validate(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Введенный Логин пользователя: {}", user.getLogin());
            throw new ValidationException("Логин пользователя не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Указанная Дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

    private void check(@RequestBody User userToAdd) {
        Collection<User> userFromCollection = users.values();
        for (User user : userFromCollection) {
            if (isAlreadyExist(userToAdd, user)) {
                log.warn("Введенный Email пользователя: {}\nСуществующий Email пользователя: {}", userToAdd, user);
                throw new ValidationException("Пользователь с таким Email или логином уже существует");
            }
        }
    }

    private boolean isAlreadyExist(User userToAdd, User user) {
        if (userToAdd.getLogin().equals(user.getLogin()) &&
                userToAdd.getEmail().equals(user.getEmail())) {
            return true;
        }
        return false;
    }
}
