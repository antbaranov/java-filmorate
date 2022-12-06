package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILMS " +
                "(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setInt(6, Math.toIntExact(film.getMpa().getId()));
            return ps;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilmById(id);
    }


    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS " +
                "SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ? , RATING_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        genreService.deleteFilmGenres(film.getId());
        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }

        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS " +
                "INNER JOIN RATING_MPA ON FILMS.RATING_ID = RATING_MPA.RATING_ID " +
                "WHERE FILM_ID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeFilm(rs), filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Фильма с id=%d нет в базе данных", filmId));

        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film deleteById(Integer id) {
        return null;
    }

    @Override
    public boolean deleteFilm(Film film) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return true;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM FILMS " +
                "INNER JOIN RATING_MPA ON FILMS.RATING_ID = RATING_MPA.RATING_ID ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT * FROM FILMS " +
                "INNER JOIN RATING_MPA ON FILMS.RATING_ID = RATING_MPA.RATING_ID ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        String sqlQuery = "SELECT * FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sqlQuery, userId, filmId);
        if (!existLike.next()) {
            String setLike = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES  (?, ?) ";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, userId, filmId);
        log.info(String.valueOf(rs.next()));
        return rs.next();
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        String deleteLike = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
        return true;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {

        String sqlQuery = "SELECT COUNT(L.LIKE_ID) AS like_rate" +
                ",FILMS.FILM_ID, FILMS.FILM_NAME, FILMS.DESCRIPTION, " +
                "FILMS.RELEASE_DATE, FILMS.DURATION, FILMS.RATE, R.RATING_ID, R.MPA_NAME, R.DESCRIPTION FROM FILMS " +
                "LEFT JOIN LIKES AS L ON L.FILM_ID = FILMS.FILM_ID " +
                "INNER JOIN RATING_MPA AS R ON R.RATING_ID = FILMS.RATING_ID " +
                "GROUP BY FILMS.FILM_ID " +
                "ORDER BY like_rate DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), count);
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("FILM_ID");
        Film film = new Film(
                filmId,
                rs.getString("FILMS.FILM_NAME"),
                rs.getString("FILMS.DESCRIPTION"),
                Objects.requireNonNull(rs.getDate("FILMS.RELEASE_DATE")).toLocalDate(),
                rs.getInt("FILMS.DURATION"),
                rs.getInt("FILMS.RATE"),
                new Mpa(rs.getInt("RATING_MPA.RATING_ID"),
                        rs.getString("RATING_MPA.MPA_NAME"),
                        rs.getString("RATING_MPA.DESCRIPTION")),
                (List<Genre>) genreService.getFilmGenres(filmId),
                getFilmLikes(filmId)
        );
        return film;
    }

    private List<Integer> getFilmLikes(Integer filmId) {
        String sqlQuery = "SELECT USER_ID FROM LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
    }
}
