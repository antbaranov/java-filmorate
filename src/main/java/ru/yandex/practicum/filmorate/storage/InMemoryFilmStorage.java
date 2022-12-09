package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int id = 1;
    private static final LocalDate DATE_BEFORE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        validate(film);
        check(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {} успешно добавлен в коллекцию", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        if (!films.containsKey(film.getId())) {
            throw new ObjectNotFoundException("Невозможно обновить данные о фильме, так как такого фильма у нас нет");
        }
        check(film);
        films.put(film.getId(), film);
        log.info("Информация о фильме: {} успешно обновлена", film.getName());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {

        Collection<Film> allFilms = films.values();
        if (allFilms.isEmpty()) {
            allFilms.addAll(films.values());
        }
        return allFilms;
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return (List<Film>) films;
    }

    @Override
    public boolean deleteFilm(Film film) {
        films.remove(film.getId());
        return true;
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.addLike(userId);
        updateFilm(film);
        return true;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.deleteLike(userId);
        updateFilm(film);
        return true;
    }

    @Override
    public List<Film> getPopularFilms(Integer size) {
        return getAllFilms()
                .stream()
                .filter(film -> film.getLikes() != null)
                .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                .limit(size)
                .collect(Collectors.toList());
    }

    void validate(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(DATE_BEFORE)) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidationException("До 28 декабря 1895 года кино не производили");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new InternalException("Продолжительность фильма не может быть меньше нуля");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Текущее описание фильма: {}", film.getDescription());
            throw new ValidationException("Описание должно быть не более 200 символов");
        }
    }

    private void check(Film filmToAdd) {
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
