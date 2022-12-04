package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);
    User getUserById(Integer id);

    User deleteUserById(Integer id);

    Collection<User> getAllUsers();

    List<Integer> addFriendship(Integer followedId, Integer followerId);

    void addFriend(Integer firstId, Integer secondId);

    void deleteFriend(Integer userId, Integer friendId);

    List<Integer> removeFriendship(int firstID, int secondId);

    Map<Integer, User> getUsers();

    List<User> getUserFriends(Integer userId);

    List<User> getFriendsListById(int id);

    List<User> getCommonFriendsList(int firstId, int secondId);
}
