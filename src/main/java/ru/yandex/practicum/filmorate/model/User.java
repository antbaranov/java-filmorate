package ru.yandex.practicum.filmorate.model;

import jdk.jfr.SettingDefinition;
import lombok.*;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
@Valid
public class User {

    private int id;

    @Email(message = "invalid email")
    private String email;


    @NotNull
    @NotBlank(message = "login must not be empty")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private List<Integer> friends;
    public boolean addFriend(Integer id) {
        return friends.add(id);
    }
    public boolean deleteFriend(Integer id) {

        return friends.remove(id);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
