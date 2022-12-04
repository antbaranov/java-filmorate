package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addFilmGenres(Integer filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            String setNewGenres = "insert into FILMS_GENRES (FILM_ID, GENRE_ID) values (?, ?) ON CONFLICT DO NOTHING";
            jdbcTemplate.update(setNewGenres, filmId, genre.getId());
        }
        return true;
    }

    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select GENRES.GENRE_ID, GENRE_NAME from GENRES " +
                "INNER JOIN FILMS_GENRES Fg ON GENRES.GENRE_ID = Fg.GENRE_ID " +
                "where FILM_ID = ?";
        return (List<Genre>) jdbcTemplate.query(sqlGenre, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
        return genre;
    }

    public List<Genre> getAllGenres() {
        String sqlGenre = "select GENRE_ID, GENRE_NAME from GENRES ORDER BY GENRE_ID";
        return (List<Genre>) jdbcTemplate.query(sqlGenre, this::makeGenre);
    }

    public boolean deleteFilmGenres(int filmId) {
        String deleteOldGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteOldGenres, filmId);
        return true;
    }

    public Genre getGenreById(int genreId) {
        String sqlGenre = "select * from GENRES where GENRE_ID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlGenre, this::makeGenre, genreId);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " + genreId + " не зарегистрирован!");
        }
        return genre;
    }
}
