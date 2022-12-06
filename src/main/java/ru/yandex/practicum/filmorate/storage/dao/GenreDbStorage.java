package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addFilmGenres(int filmId, Collection<Genre> genres) {
        for (Genre genre : genres) {
            String sqlQuery = "INSERT INTO GENRE_LINE (FILM_ID, GENRE_ID) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        }
        return true;
    }

    public Collection<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT GENRES.GENRE_ID, GENRE_NAME FROM GENRES " +
                "INNER JOIN GENRE_LINE ON GENRES.GENRE_ID = GENRE_LINE.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT GENRE_ID, GENRE_NAME FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    public boolean deleteFilmGenres(int filmId) {
        String sqlQuery = "DELETE FROM GENRE_LINE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        return true;
    }

    public Genre getGenreById(int genreId) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с id " + genreId + " не найден!");
        }
        return genre;
    }


}
