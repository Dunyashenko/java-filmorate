package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable int id) {
        return filmStorage.findOne(id);
    }

    @GetMapping("/popular")
    public List<Film> getTopPopularFilms(@RequestParam int count) {
        return filmService.getTopPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        return filmStorage.create(newFilm);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmStorage.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public List<Integer> addLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public List<Integer> deleteLikeFromFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLikeFromFilm(id, userId);
    }


}
