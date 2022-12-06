package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM RATING_MPA";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("RATING_ID"),
                rs.getString("MPA_NAME"),
                rs.getString("DESCRIPTION"));
    }

    public Mpa getMpaById(int mpaId) {
        String sqlQuery = "SELECT * FROM RATING_MPA WHERE RATING_ID = ?";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlQuery, this::makeMpa, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Возрастной рейтинг с id: '" +
                    mpaId + "' не найден!");
        }
        return mpa;
    }

}
