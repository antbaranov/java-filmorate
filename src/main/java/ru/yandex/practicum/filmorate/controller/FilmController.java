package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Получен POST запрос по адресу /films на добавление фильма: Данные запроса: '{}'", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновлен фильм с id '{}' '{}'", film.getId(), film);
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