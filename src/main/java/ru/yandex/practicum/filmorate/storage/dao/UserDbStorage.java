package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Map;

@Component
public class UserDbStorage implements UserStorage {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User getById(int id) {
        return null;
    }

    @Override
    public User deleteById(int id) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return null;
    }
}
