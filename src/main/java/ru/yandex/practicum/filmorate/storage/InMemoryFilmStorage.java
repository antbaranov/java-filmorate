package ru.yandex.practicum.filmorate.storage;

import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int id = 1;
    private static final LocalDate DATE_BEFORE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        validate(film);
        check(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {} успешно добавлен в коллекцию", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
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
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film getById(int id) {
        return  films.get(id);
    }

    @Override
    public Film deleteById(int id) {
        Film film = films.get(id);
        films.remove(id);
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    void validate(Film film) {
        if (film.getReleaseDate().isBefore(DATE_BEFORE) || film.getDuration() < 0) {
            log.warn("Дата выпуска фильма: {}\nПродолжительность фильма: {}", film.getReleaseDate(), film.getDuration());
            throw new InternalException("До 28 декабря 1895 года кино не производили или продолжительность неверная");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Текущее описание фильма: {}", film.getDescription());
            throw new InternalException("Описание должно быть не более 200 символов");
        }

    }

    private void check(Film filmToAdd) {
        boolean exists = films.values().stream()
                .anyMatch(film -> isAlreadyExist(filmToAdd, film));
        if (exists) {
            log.warn("Фильм к добавлению: {}", filmToAdd);
            throw new InternalException("Такой фильм уже существует в коллекции");
        }
    }

    private boolean isAlreadyExist(Film filmToAdd, Film film) {
        return filmToAdd.getName().equals(film.getName()) &&
                filmToAdd.getReleaseDate().equals(film.getReleaseDate());
    }
}
