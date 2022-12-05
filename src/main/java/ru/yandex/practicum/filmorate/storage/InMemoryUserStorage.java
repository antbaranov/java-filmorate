package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        validate(user);
        check(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен с логином: {}, email: {}", user.getLogin(), user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("Невозможно обновить данные, так как пользователя не существует");
        }
        check(user);
        users.put(user.getId(), user);
        log.info("Данные пользователя с id: {}, логином: {} успешно обновлена", user.getId(), user.getLogin());
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {

        return users.values();
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public User deleteUserById(Integer id) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    @Override
    public boolean deleteUser(User user) {
        users.remove(user.getId());
        return true;
    }

    void validate(User user) {

        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.warn("Введенный Логин пользователя: '{}'", user.getLogin());
            throw new ValidationException("Логин не может быть пустым");
        }

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
            log.warn("Не заполнено Имя пользователя заменено на Логин: '{}'", user.getName());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Указанная Дата рождения: '{}'", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (user.getEmail().isBlank() || user.getEmail() == null || user.getEmail().equals(" ")) {
            log.warn("Введенный Email пользователя: '{}'", user.getEmail());
            throw new ValidationException("Email не может быть пустым");
        }
    }

    public List<Integer> addFriendship(Integer id, Integer otherId) {
        return null;
    }


    @Override
    public boolean addFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        updateUser(user);
        updateUser(friend);
        return true;
    }

    @Override
    public boolean deleteFriend(Integer userId, Integer friendId) {
        return false;
    }

    private void check(User userToAdd) {
        boolean exists = users.values().stream()
                .anyMatch(user -> isAlreadyExist(userToAdd, user));
        if (exists) {
            log.warn("Введенный Email пользователя: '{}'", userToAdd);
            throw new ValidationException("Пользователь с таким Email или логином уже существует");
        }
    }

    private boolean isAlreadyExist(User userToAdd, User user) {
        return userToAdd.getLogin().equals(user.getLogin()) ||
                userToAdd.getEmail().equals(user.getEmail());

    }

    public List<Integer> removeFriendship(int firstID, int secondId) {
        return null;
    }

    public List<User> getFriendsListById(int id) {
        return null;
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        return null;
    }

    @Override
    public User getUser(final Integer id) {
        return users.get(id);
    }
}
