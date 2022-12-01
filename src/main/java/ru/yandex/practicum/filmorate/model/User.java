package ru.yandex.practicum.filmorate.model;

import jdk.jfr.SettingDefinition;
import lombok.*;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
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
    private Set<Integer> friends;

    public void addFriend(Integer id) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(id);
    }

    public Set<Integer> getFriendsId() {
        if (friends == null) {
            friends = new HashSet<>();
        }
        return friends;
    }
}
