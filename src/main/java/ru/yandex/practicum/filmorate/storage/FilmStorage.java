package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film getById(int id);
    Film deleteById(int id);

    Collection<Film> findAll();
    Map<Integer, Film> getFilms();
}
