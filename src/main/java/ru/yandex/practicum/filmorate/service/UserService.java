package ru.yandex.practicum.filmorate.service;

import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private int counter = 1;
    private final Validator validator;
    private final UserStorage userStorage;



    @Autowired
    public UserService(Validator validator, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        log.info("Список пользователей отправлен");
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        validate(user);
        log.info("Создан пользователь");
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        log.info("Обновлен пользователь");
        return userStorage.updateUser(user);
    }



    public User getUserById(Integer id) {
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
        return userStorage.deleteUserById(id);
    }

    // *** Add Friend to friends list
    public void addFriend(final String supposedUserId, final String supposedFriendId) {
        User user = getUserStored(supposedUserId);
        User friend = getUserStored(supposedFriendId);
        userStorage.addFriend(user.getId(), friend.getId());
        log.info("Пользователь с id: '{}' добавлен с список друзей пользователя с id: '{}'", supposedUserId, supposedFriendId);
    }

    // *** Delete the friend from friends list
    public void deleteFriend(final String supposedUserId, final  String supposedFriendId) {
        User user = getUserStored(supposedUserId);
        User friend = getUserStored(supposedFriendId);
        userStorage.deleteFriend(user.getId(), friend.getId());
        log.info("Пользователь с id: '{}' добавлен с список друзей пользователя с id: '{}'", supposedUserId, supposedFriendId);
    }

    // *** Get all friends of the user
    public Collection<User> getUserFriends(String userId) {
        User user = getUserStored(userId);
        Collection<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

    public User getUserById(final String supposedId) {
        return getUserStored(supposedId);
    }

    private User getUserStored(final String supposedId) {
        final int userId = parseId(supposedId);
        if (userId == Integer.MIN_VALUE) {
            throw new NotFoundException("Не удалось найти id пользователя: " +
                    "значение " + supposedId);
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " +
                    userId + " не зарегистрирован!");
        }
        return user;
    }


    private Integer parseId(final String id) {
        try {
            return Integer.valueOf(id);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    public void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: '{}', email: '{}'", text, user.getName(), user.getEmail());
    }

    private void validate(final User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не задано. Установлено значение {} из поля login", user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не содержит буквенных символов. " +
                    "Установлено значение {} из поля login", user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидации Пользователя: " + messageBuilder, violations);
        }
        if (user.getId() == 0) {
            user.setId(counter++);
        }
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

    public Collection<User> getCommonFriendsList(final String supposedUserId, final String supposedOtherId) {
        User user = getUserStored(supposedUserId);
        User otherUser = getUserStored(supposedOtherId);
        Collection<User> commonFriends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUser(id));
            }
        }
        return commonFriends;
    }

}
