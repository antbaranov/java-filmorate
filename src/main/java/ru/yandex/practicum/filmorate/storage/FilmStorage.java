package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film create(Film film);
    Film update(Film film);
    Film getFilmById(Integer id);
    Film deleteById(Integer id);
    Collection<Film> getAllFilms();
    Map<Integer, Film> getFilms();
    boolean deleteFilm(Film film);
    boolean addLike(Integer filmId, Integer userId);
    boolean deleteLike(Integer filmId, Integer userId);
    Collection<Film> getFilmsPopular(Integer count);
}
