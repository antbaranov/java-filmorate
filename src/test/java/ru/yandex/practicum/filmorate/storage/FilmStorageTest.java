package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final FilmStorage filmStorage;

    private final Film film = Film.builder()
            .id(3)
            .name("film1 name")
            .description("film1 description")
            .releaseDate(LocalDate.now().minusYears(10))
            .duration(7)
            .mpa(2, "PG")
            .genres("Драма")
            .directors("Director")
            .build();


    @Test
    public void addFilmTest() {
        filmStorage.save(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
    }

    @Test
    public void getFilmByIdTest() {
        filmStorage.save(film);
        Film dbFilm = filmStorage.getFilmById(1);
        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void updateFilmTest() {
        Film added = filmStorage.save(film);
        added.setName("film updated");
        filmStorage.updateFilm(added);
        Film dbFilm = filmStorage.getFilmById(added.getId());
        assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "film updated");
    }

    @Test
    public void deleteFilmTest() {
        Film addedFilm1 = filmStorage.save(film1);
        Film addedFilm2 = filmStorage.save(film2);
        Collection<Film> beforeDelete = filmStorage.getAllFilms();
        filmStorage.deleteFilm(addedFilm1);
        Collection<Film> afterDelete = filmStorage.getAllFilms();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}