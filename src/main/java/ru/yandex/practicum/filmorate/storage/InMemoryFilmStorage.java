package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidationServiceImpl;
import ru.yandex.practicum.filmorate.service.Validate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Validate filmValidator = new FilmValidationServiceImpl();
    private int currentMaxId = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findOne(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            log.error("film with id = {} is not found", id);
            throw new NotFoundException("film with id = " + id + " is not found");
        }
    }

    @Override
    public Film create(Film newFilm) {
        log.info("Starting creation of film = {}", newFilm);
        filmValidator.checkRules(newFilm);

        newFilm.setId(getNextId());
        log.info("Film with id {} has successfully been created", newFilm.getId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film update(Film newFilm) {
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

    @Override
    public Integer delete(int id) {
        log.debug("Deleting film with id {}", id);
        films.remove(id);
        return id;
    }

    private int getNextId() {
        return ++currentMaxId;
    }
}
