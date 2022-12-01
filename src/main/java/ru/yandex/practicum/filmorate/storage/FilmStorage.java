package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(int id);
    Film deleteById(int id);

    Collection<Film> getAllFilms();
    Map<Integer, Film> getFilms();

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    List<Film> getFilmsPopular(Integer count);
}
