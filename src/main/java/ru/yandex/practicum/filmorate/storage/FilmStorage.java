package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;

public interface FilmStorage {

    @PostMapping
    public Film create(@Valid @RequestBody Film film);

    @PutMapping
    public Film put(@Valid @RequestBody Film film);

    @DeleteMapping
    Film delete(@Valid @RequestBody Film film);

    @GetMapping
    public Collection<Film> findAll()
}
