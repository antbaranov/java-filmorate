package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Set<Integer> usersLikes = new HashSet<>();

    private int id;
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200, message = "Описание не может быть больше 200 символов")
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Min(value = 1)
    private final int duration;
}
