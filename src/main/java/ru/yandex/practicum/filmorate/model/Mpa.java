package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {

    @PositiveOrZero(message = "id can NOT be negative")
    private int id;

    @NotBlank(message = "Name must not be empty")
    @NotNull(message = "Name must not be null")
    private String name;
    private String description;
}
