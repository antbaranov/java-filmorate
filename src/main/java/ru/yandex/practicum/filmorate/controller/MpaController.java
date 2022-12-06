package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired(required = false)
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> getAllMpa() {
        log.info("Получен запрос GET по адресу: '/mpa'");
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable String id) {
        log.info("Получен запрос GET по адресу: '/mpa/{}'", id);
        return mpaService.getMpaById(id);
    }
}
