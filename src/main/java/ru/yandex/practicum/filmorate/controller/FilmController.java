package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired(required = false)
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Получен POST запрос: Данные тела запроса: '{}'", film);
        log.info("Создан фильм с id: {}", film.getId());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновлен фильм '{}'", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен GET запрос по адресу '/films'");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable String id) {
        log.info("Получен GET запрос по адресу '/films/{}'", id);
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public Film deleteById(@PathVariable Integer id) {
        log.info("Фильм с id: '{}' удален", id);
        return filmService.deleteById(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable String filmId, @PathVariable String userId) {
        log.info("Добавлен лайк на фильим '{}' от пользователя '{}'", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable String filmId, @PathVariable String userId) {
        log.info("Удален лайк на фильим '{}' от пользователя '{}'", filmId, userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("Получен GET запрос по адресу '/films/popular?count={}'", count);
        return filmService.getPopularFilms(count);
    }
}