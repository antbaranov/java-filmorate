package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @PositiveOrZero(message = "id can Not be negative")
    private int id;
    @NotBlank(message = "name must not be empty")
    private String name;
    @NotBlank
    @Size(min = 10, max = 200, message = "description length must be between 10 and 200")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(value = 1, message = "duration must be more 1")
    private int duration;
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Integer> likes = new HashSet<>();

    public void addLike(Integer id) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }
}
