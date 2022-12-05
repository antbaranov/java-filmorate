package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 10, max = 200, message = "description length must be between 10 and 200")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(value = 1, message = "duration must be more 1")
    private Integer duration;
    private int rate;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    private List<Integer> likes = new ArrayList<>();

    public boolean addLike(Integer id) {
        return likes.add(id);
    }

    public boolean deleteLike(Integer id) {

        return likes.remove(id);
    }
}
