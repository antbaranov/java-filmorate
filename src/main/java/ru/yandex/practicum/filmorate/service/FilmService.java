package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private static int counter = 1;
    private final Validator validator;
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate START_DATA = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(Validator validator, FilmStorage filmStorage,
                       @Autowired(required = false) UserService userService) {
        this.validator = validator;
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getAllFilms() {
        log.info("Список фильмов отправлен");
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        validate(film);
        validateReleaseDate(film, "");
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        validateReleaseDate(film, "");
        return filmStorage.updateFilm(film);
    }

    public void addLike(String filmId, String userId) {
        Film film = getFilmStored(filmId);
        User user = userService.getUserById(userId);
        filmStorage.addLike(film.getId(), user.getId());
        log.info("Фильм с id: '{}' получил лайк", filmId);
    }

    public void deleteLike(String filmId, String userId) {
        Film film = getFilmStored(filmId);
        User user = userService.getUserById(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
        log.info("У Фильм с id: '{}' удалён лайк", filmId);
    }

    public Collection<Film> getPopularFilms(String count) {
        Integer size = parseId(count);
        if (size == Integer.MIN_VALUE) {
            size = 10;
        }
        log.info("Список популярных фильмов отправлен");
        return filmStorage.getPopularFilms(size);
    }

    public void validateReleaseDate(Film film, String text) {
        if (film.getReleaseDate().isBefore(START_DATA)) {
            throw new ValidationException("Дата релиза не может быть раньше: " + START_DATA);
        }
        log.debug("{} фильм: '{}'", text, film.getName());
    }

    private void validate(Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<Film> filmConstraintViolation : violations) {
                messageBuilder.append(filmConstraintViolation.getMessage());
            }
            throw new FilmValidationException("Ошибка валидации Фильма: " + messageBuilder, violations);
        }
        if (film.getId() == 0) {
            film.setId(getNextId());
        }
    }

    private static int getNextId() {
        return counter++;
    }

    public Film getFilmById(String id) {
        log.info("Фильм с id: '{}' отправлен", id);
        return getFilmStored(id);
    }

    private Integer parseId(final String supposedInt) {
        try {
            return Integer.valueOf(supposedInt);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    private Film getFilmStored(final String supposedId) {
        final int filmId = parseId(supposedId);
        if (filmId == Integer.MIN_VALUE) {
            throw new NotFoundException("Не удалось найти id фильма: '{}'", supposedId);
        }
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с id: '%d' не найден", filmId));
        }
        return film;
    }
}
