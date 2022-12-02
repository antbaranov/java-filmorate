package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addFilmGenres(Integer filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            String setNewGenres = "insert into FILMS_GENRES (FILM_ID, GENRE_ID) values (?, ?) on CONFLICT DO NOTHING";
            jdbcTemplate.update(setNewGenres, filmId, genre.getId());
        }
        return true;
    }

    public Set<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select GENRES.GENRE_ID, GENRE_NAME from GENRES " +
                "INNER JOIN FILMS_GENRES Fg ON GENRES.GENRE_ID = Fg.GENRE_ID " +
                "where FILM_ID = ?";
        return (Set<Genre>) jdbcTemplate.query(sqlGenre, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
        return genre;
    }

    public Set<Genre> getAllGenres() {
        String sqlGenre = "select GENRE_ID, GENRE_NAME from GENRES ORDER BY GENRE_ID";
        return (Set<Genre>) jdbcTemplate.query(sqlGenre, this::makeGenre);
    }
}
