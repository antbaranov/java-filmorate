package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

public interface UserStorage {

    @PostMapping
    public User create(@Valid @RequestBody User user);

    @PutMapping
    public User put(@Valid @RequestBody User user);

    @DeleteMapping
    User delete(@Valid @RequestBody User user);

    @GetMapping
    public Collection<User> findAll();
}
