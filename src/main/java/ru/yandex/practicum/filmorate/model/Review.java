package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Review {
    private long reviewId;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private long userId;
    @NotNull
    private long filmId;
    private long useful;
}
