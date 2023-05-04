package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Collection<Genre> getAllGenres() {
        return genreStorage.findAll();
    }

    public Genre getGenreById(int id) {
        Genre genre = genreStorage.findGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден!"));
        return genre;
    }
}
