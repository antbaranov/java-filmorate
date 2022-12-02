package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final GenreService genreService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILMS " +
                "(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, Math.toIntExact(film.getMpa().getId()));
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
        return null;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String sqlFilm = "select * from FILMS " +
                "INNER JOIN RATING_MPA R on FILMS.RATING_ID = R.RATING_ID " +
                "where FILM_ID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), filmId);
        }
        catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильма с id=%d нет в базе данных", filmId));

        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film deleteById(Integer id) {
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return null;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {

    }

    @Override
    public List<Film> getFilmsPopular(Integer count) {
        return null;
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("FILM_ID");
        Film film = new Film(
                filmId,
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                Objects.requireNonNull(rs.getDate("RELEASE_DATE")).toLocalDate(),
                rs.getInt("DURATION"),
                new Mpa(rs.getInt("RATING_MPA.RATING_ID"),
                        rs.getString("RATING_MPA.MPA_NAME"),
                        rs.getString("RATING_MPA.DESCRIPTION")),
                (Set<Genre>) genreService.getFilmGenres(filmId),
                (Set<Integer>) getFilmLikes(filmId)
        );
        return film;
    }

    private List<Integer> getFilmLikes(Integer filmId) {
        String sqlGetLikes = "select USER_ID from LIKES where FILM_ID = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
        return likes;
    }
}
