package ru.yandex.practicum.filmorate.model;

import jdk.jfr.SettingDefinition;
import lombok.*;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
@Valid
public class User {

    @PositiveOrZero(message = "id can Not be negative")
    private int id;
    @NotNull(message = "login must not be null")
    @NotBlank(message = "login must not be empty")
    @Email(message = "invalid email")
    private String email;
    @NotNull
    @NotBlank(message = "login must not be empty")
    private String login;
    @NotBlank(message = "name must not be empty")
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private List<Integer> friends;

    public User(int id, String email, String login, String name, LocalDate birthday) {
    }

    public void addFriend(Integer id) {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(id);
    }

    public List<Integer> getFriendsId() {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        return friends;
    }
}
