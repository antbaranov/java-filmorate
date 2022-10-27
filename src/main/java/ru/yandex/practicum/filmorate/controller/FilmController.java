package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private static final LocalDate DATE_BEFORE = LocalDate.of(1895, 12, 28);
      private final Map<Integer, Film> films = new HashMap();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        check(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {} успешно добавлен в коллекцию", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validate(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Невозможно обновить данные о фильме, так как такого фильма у нас нет");
        }
        check(film);
        films.put(film.getId(), film);
        log.info("Информация о фильме: {} успешно обновлена", film.getName());
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    private void validate(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATE_BEFORE) || film.getDuration() < 0) {
            log.warn("Дата выпуска фильма: {} /n Продолжительность фильма: {}", film.getReleaseDate(), film.getDuration());
            throw new ValidationException("До 28 декабря 1895 кино не производили или продолжительность неверная");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Описание: {}", film.getDescription());
            throw new ValidationException("Описание должно быть не более 200 символов");
        }
    }

    private void check(@RequestBody Film film) {
        Collection<Film> filmCollection = films.values();
        for (Film flm : filmCollection) {
            if (film.getName().equals(flm.getName()) && film.getReleaseDate().equals(flm.getReleaseDate())) {
                log.warn("Фильм: {}/n Фильм: {}", film, flm);
                throw new ValidationException("Такой фильм уже добавлен");
            }
        }
    }
}
