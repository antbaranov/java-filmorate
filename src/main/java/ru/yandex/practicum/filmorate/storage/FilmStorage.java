package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film create(Film film);
    Film update(Film film);
    Film getFilmById(int id);
    Film deleteById(Integer id);
    Collection<Film> getAllFilms();
    List<Film> getFilms();
    boolean deleteFilm(Film film);
    boolean addLike(int filmId, int userId);
    boolean deleteLike(int filmId, int userId);
    Collection<Film> getPopularFilms(Integer count);
}
