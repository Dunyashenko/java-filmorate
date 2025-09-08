package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidationServiceImpl;
import ru.yandex.practicum.filmorate.service.Validate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Validate filmValidator = new FilmValidationServiceImpl();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        log.info("Starting creation of film = {}", newFilm);
        filmValidator.checkRules(newFilm);

        newFilm.setId(getNextId());
        log.info("Film with id {} has successfully been created", newFilm.getId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("User without id cannot be updated");
            throw new ConditionNotMetException("Id should be defined");
        }

        if (films.containsKey(newFilm.getId())) {
            log.info("Starting updating film with id {}", newFilm.getId());
            Film film = films.get(newFilm.getId());

            if (newFilm.getName() != null) {
                log.debug("Updating old field name from {} to a new {}", film.getName(), newFilm.getName());
                film.setName(newFilm.getName());
            }

            if (newFilm.getDescription() != null) {
                FilmValidationServiceImpl.checkDescriptionCondition(newFilm.getDescription());
                log.debug("Updating old field description from {} to a new {}", film.getDescription(), newFilm.getDescription());
                film.setDescription(newFilm.getDescription());
            }

            if (newFilm.getReleaseDate() != null) {
                FilmValidationServiceImpl.checkReleaseDateCondition(newFilm.getReleaseDate());
                log.debug("Updating old field release date from {} to a new {}", film.getReleaseDate(), newFilm.getReleaseDate());
                film.setReleaseDate(newFilm.getReleaseDate());
            }

            if (newFilm.getDuration() != null) {
                FilmValidationServiceImpl.checkDurationCondition(newFilm.getDuration());
                log.debug("Updating old field duration from {} to a new {}", film.getDuration(), newFilm.getDuration());
                film.setDuration(newFilm.getDuration());
            }
            log.info("Film with id {} has successfully been updated", newFilm.getId());
            return film;
        } else {
            log.error("The film with id = " + newFilm.getId() + " cannot be found");
            throw new NotFoundException("The film with id = " + newFilm.getId() + " cannot be found");
        }
    }

    private int getNextId() {
        int currentId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }


}
