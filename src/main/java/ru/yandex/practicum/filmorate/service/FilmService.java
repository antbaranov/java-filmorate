package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate START_DATA = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Autowired(required = false) UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film createFilm(Film film) {
        validateReleaseDate(film, "Добавлен");
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        validateReleaseDate(film, "Обновлён");
        return filmStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        log.info("Список фильмов отправлен");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        log.info("Фильм с id: '" + id + "' отправлен");
        return filmStorage.getFilmById(id);
    }

    public Film deleteById(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не найден, удаление невозможно");
        }
        log.info("Фильм с id: '" + id + "' удалён");
        return filmStorage.deleteById(id);
    }

    public List<Film> getFilmsPopular(Integer count) {
        return filmStorage.getFilmsPopular(count);
    }

    public void addLike(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        filmStorage.addLike(filmId, userId);
        log.info("Фильм с id: '{}' получил лайк", filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("У Фильм с id: '{}' удалён лайк", filmId);
    }

    public Film removeLike(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new ObjectNotFoundException("Нет лайка от пользователя");
        }
        filmStorage.getFilmById(filmId).getLikes().contains(userId);
        log.info("Пользователь с id: {} удалил лайк фильму с id {}", userId, filmId);
        return filmStorage.getFilmById(filmId);
    }
    public List<Film> getPopularFilms(int count) {
        log.info("Список популярных фильмов отправлен");

        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void validateReleaseDate(Film film, String text) {
        if (film.getReleaseDate().isBefore(START_DATA)) {
            throw new ValidationException("Дата релиза не может быть раньше: " + START_DATA);
        }
        log.debug("{} фильм: '{}'", text, film.getName());
    }
}
