package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Map;

@Component
public class FilmDbStorage implements FilmStorage {
    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film getById(int id) {
        return null;
    }

    @Override
    public Film deleteById(int id) {
        return null;
    }

    @Override
    public Collection<Film> findAll() {
        return null;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return null;
    }
}
