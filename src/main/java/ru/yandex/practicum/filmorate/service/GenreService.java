package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;


    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public boolean addFilmGenres(int filmId, Set<Genre> genres) {
        return genreDbStorage.addFilmGenres(filmId, genres);
    }

    public Set<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Set<Genre> getFilmGenres(int filmId) {
        return genreDbStorage.getGenresByFilmId(filmId);
    }
}
