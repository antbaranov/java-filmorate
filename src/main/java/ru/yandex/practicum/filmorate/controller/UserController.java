package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable int id) {
        return userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFriendship(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        return userService.getFriendsListById(id);
    }

    @GetMapping("/{firstId}/friends/common/{secondId}")
    public List<User> getCommonFriends(@PathVariable int firstId, @PathVariable int secondId) {
        return userService.getCommonFriendsList(firstId, secondId);
    }
}