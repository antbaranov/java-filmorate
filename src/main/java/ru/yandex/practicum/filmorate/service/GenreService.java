package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.Collection;
import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;


    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public boolean addFilmGenres(int filmId, List<Genre> genres) {
        return genreDbStorage.addFilmGenres(filmId, genres);
    }

    public Collection<Genre> getAllGenres() {

        return genreDbStorage.getAllGenres();
    }

    public Collection<Genre> getFilmGenres(int filmId) {

        return genreDbStorage.getGenresByFilmId(filmId);
    }

    public boolean deleteFilmGenres(int filmId) {

        return genreDbStorage.deleteFilmGenres(filmId);
    }

    public Genre getGenreById(String strId) {
        int id = parseId(strId);
        return genreDbStorage.getGenreById(id);
    }

    private Integer parseId(final String strId) {
        try {
            return Integer.valueOf(strId);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }
}
