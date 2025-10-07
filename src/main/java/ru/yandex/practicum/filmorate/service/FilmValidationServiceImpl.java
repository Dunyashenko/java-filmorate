package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ConditionNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidationServiceImpl implements Validate {

    @Override
    public void checkRules(Object o) {
        Film film = (Film) o;
        log.debug("Validating adherence to film field rules for film " + film.toString());
        checkNameCondition(film.getName());
        checkDescriptionCondition(film.getDescription());
        checkReleaseDateCondition(film.getReleaseDate());
        checkDurationCondition(film.getDuration());
    }

    public static void checkNameCondition(String name) {
        if (name == null || name.isBlank()) {
            log.error("Film name is not defined");
            throw new ConditionNotMetException("Film name should be defined");
        }
    }

    public static void checkDescriptionCondition(String description) {
        if (description.toCharArray().length > 200) {
            log.error("Length of description is " + description.toCharArray().length);
            throw new ConditionNotMetException("Length of film description should be 200 chars or less");
        }
    }

    public static void checkReleaseDateCondition(LocalDate date) {
        if (date.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Date of film release is before than expected and equals" + date);
            throw new ConditionNotMetException("Film release should be after than 1895-12-28");
        }
    }

    public static void checkDurationCondition(Integer duration) {
        if (duration < 0) {
            log.error("Duration field has a negative value and equals " + duration);
            throw new ConditionNotMetException("Film duration should be a positive number");
        }
    }
}
