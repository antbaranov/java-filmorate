package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        final String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "VALUES ( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (user.getFriends() != null) {
            for (Integer friendId : user.getFriends()) {
                addFriend(user.getId(), friendId);
            }
        }
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        final String sqlQuery = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }


    @Override
    public User getUserById(Integer id) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id: '" +
                    id + "' не зарегистрирован!");
        }
        return user;
    }

    private List<Integer> getIdsFriends(int userId) {
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, userId);
    }

    @Override
    public User deleteUserById(Integer id) {
        final String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        User user = getUserById(id);
        jdbcTemplate.update(sqlQuery, id);
        return user;
    }

    @Override
    public boolean deleteUser(User user) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, user.getId()) > 0;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public List<Integer> addFriendship(Integer followedId, Integer followerId) {
        return null;
    }

    @Override
    public boolean addFriend(Integer userId, Integer friendId) {
        boolean friendAccepted;
        String sqlGetReversFriend = "SELECT * FROM FRIENDSHIP " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);
        friendAccepted = rs.next();
        String sqlSetFriend = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID, STATUS) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);
        if (friendAccepted) {
            String sqlSetStatus = "UPDATE FRIENDSHIP SET STATUS = true " +
                    "WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
        return friendAccepted;
    }

    @Override
    public boolean deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        String sqlSetStatus = "UPDATE FRIENDSHIP SET STATUS = false " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlSetStatus, friendId, userId);
        return false;
    }

    @Override
    public List<Integer> removeFriendship(int firstID, int secondId) {
        final String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, firstID, secondId);
        return List.of(firstID, secondId);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return null;
    }

    @Override
    public List<User> getFriendsListById(int id) {
        return null;
    }

    @Override
    public List<User> getCommonFriendsList(int firstId, int secondId) {
        return null;
    }

    @Override
    public User getUser(Integer id) {
        String sqlUser = "SELECT * FROM USERS WHERE USER_ID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id: " +
                    id + " не зарегистрирован!");
        }
        return user;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int userId = rs.getInt("USER_ID");
        return new User(
                userId,
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                Objects.requireNonNull(rs.getDate("BIRTHDAY")).toLocalDate(),
                getUserFriends(userId));
    }

    private List<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?";
        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
    }

    private void validate(int firstID, int secondId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet followingRow = jdbcTemplate.queryForRowSet(sqlQuery, firstID);
        SqlRowSet followerRow = jdbcTemplate.queryForRowSet(sqlQuery, secondId);
        if (!followingRow.next() || !followerRow.next()) {
            throw new ObjectNotFoundException("Пользователи не найдены");
        }
    }

}
