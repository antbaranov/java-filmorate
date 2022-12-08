package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    private GenreDbStorage genreStorage;

    @Test
    void getAllGenresTest() {
        Collection<Genre> genre = genreStorage.getAllGenres();
        Assertions.assertThat(genre)
                .extracting(Genre::getName)
                .containsAll(Arrays.asList("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик"));
    }

    @Test
    void getGenresByFilmId() {
    }

    @Test
    void getGenreById() {
    }

    @Test
    void addFilmGenres() {
    }

    @Test
    void deleteFilmGenres() {
    }
}