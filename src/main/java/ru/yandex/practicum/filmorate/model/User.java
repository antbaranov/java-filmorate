package ru.yandex.practicum.filmorate.model;

import jdk.jfr.SettingDefinition;
import lombok.*;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
public class User {

       private int id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
}
