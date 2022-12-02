package ru.yandex.practicum.filmorate.service;

import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        setUserNameByLogin(user, "Добавлен");
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        setUserNameByLogin(user, "Обновлен");
        return userStorage.update(user);
    }

    public Collection<User> getAllUsers() {
        log.info("Список пользователей отправлен");
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        log.info("Пользователь с id: '{}' отправлен", id);
        return userStorage.getUserById(id);
    }

    public User deleteById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден. Невозможно удалить неизветсного пользователя");
        }
        log.info("Пользователь с id: '{}' удален", id);
        return userStorage.deleteById(id);
    }

    // *** Add Friend to friends list
    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.debug("Пользователь с id: '{}' добавлен с список друзей пользователя с id: '{}'", userId, friendId);
    }

    // *** Delete the friend from friends list
    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.debug("Пользователь с id: '{}' добавлен с список друзей пользователя с id: '{}'", userId, friendId);
    }

    // *** Get all friends of the user
    public List<User> getUserFriends(Integer userId) {
        return userStorage.getUserFriends(userId);
    }

    // *** Get mutual friends with another friend
    public Set<User> getMutualFriends(Integer userId, Integer otherId) {
        return getUserById(userId).getFriendsId()
                .stream()
                .filter(getUserById(otherId).getFriendsId()::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    public void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: '{}', email: '{}'", text, user.getName(), user.getEmail());
    }

    public List<User> addFriend(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new ObjectNotFoundException(String.format("Пользователя с id: %d или с id: %d не существует", firstId, secondId));
        }
        if (userStorage.getUserById(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи уже и так являются друзьями");
        }
        userStorage.getUserById(firstId).getFriends().add(secondId);
        userStorage.getUserById(secondId).getFriends().add(firstId);
        log.info("Пользователи: '{}' и '{}' теперь являются друзьями :)",
                userStorage.getUserById(firstId).getName(),
                userStorage.getUserById(secondId).getName());
        return Arrays.asList(userStorage.getUserById(firstId), userStorage.getUserById(secondId));
    }

    public List<User> deleteFriend(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new ObjectNotFoundException(String.format("Пользователя с id: %d или с id: %d не существует", firstId, secondId));
        }
        if (!userStorage.getUserById(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи не являются друзьями");
        }
        userStorage.getUserById(firstId).getFriends().remove(secondId);
        userStorage.getUserById(secondId).getFriends().remove(firstId);
        log.info("Пользователи: '{}' и '{}' больше не друзья :(",
                userStorage.getUserById(firstId).getName(),
                userStorage.getUserById(secondId).getName());
        return Arrays.asList(userStorage.getUserById(firstId), userStorage.getUserById(secondId));
    }


    public List<User> getFriendsListById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Нам очень жаль, невозможно получить список друзей пользователя, " +
                    "так как пользователь не найден :(");
        }
        log.info("Успех! Запрос получения списка друзей пользователя '{}' выполнен успешно :)",
                userStorage.getUserById(id).getName());
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id: %d или с id: %d не существует :(", firstId, secondId));
        }
        User firstUser = userStorage.getUserById(firstId);
        User secondUser = userStorage.getUserById(secondId);
        log.info("Список общих друзей пользователей: '{}' и '{}' успешено отправлен",
                firstUser.getName(), secondUser.getName());
        return firstUser.getFriends().stream()
                .filter(friendId -> secondUser.getFriends().contains(friendId))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }


}
