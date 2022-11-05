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
    final Map<Integer, Film> films = new HashMap<>();

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

    void validate(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATE_BEFORE) || film.getDuration() < 0) {
            log.warn("Дата выпуска фильма: {}\nПродолжительность фильма: {}", film.getReleaseDate(), film.getDuration());
            throw new ValidationException("До 28 декабря 1895 года кино не производили или продолжительность неверная");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Текущее описание фильма: {}", film.getDescription());
            throw new ValidationException("Описание должно быть не более 200 символов");
        }

    }

    void validate2(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATE_BEFORE) || film.getDuration() < 0) {
            log.warn("film.getReleaseDate film release date: '{}'\n film.getDuration film duration: {}", film.getReleaseDate(), film.getDuration());
            throw new ValidationException("В то время кино еще не было или продолжительность неверная");
        }
        Collection<Film> filmCollection = films.values();
        for (Film fl : filmCollection) {
            if (film.getName().equals(fl.getName()) && film.getReleaseDate().equals(fl.getReleaseDate())) {
                log.warn("film film: '{}'\n fl film: {}", film, fl);
                throw new ValidationException("Такой фильм уже есть");
            }
        }
    }

    private void check(@RequestBody Film filmToAdd) {
        boolean exists = films.values().stream()
                .anyMatch(film -> isAlreadyExist(filmToAdd, film));
        if (exists) {
            log.warn("Фильм к добавлению: {}", filmToAdd);
            throw new ValidationException("Такой фильм уже существует в коллекции");
        }
    }

    private boolean isAlreadyExist(Film filmToAdd, Film film) {
        return filmToAdd.getName().equals(film.getName()) &&
                filmToAdd.getReleaseDate().equals(film.getReleaseDate());
    }
}