package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ConditionNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmValidationServiceImplTest {

    @Test
    public void shouldValidateTheRuleOfFieldsForFilmCreationPositive() {
        Film film = new Film();
        film.setId(1);
        film.setName("Movie name");
        film.setDescription("Description of movie");
        film.setReleaseDate(LocalDate.of(2025, 3, 14));
        film.setDuration(90);

        Validate filmValidator = new FilmValidationServiceImpl();
        assertDoesNotThrow(() -> filmValidator.checkRules(film));
    }

    @ParameterizedTest
    @MethodSource("filmFields")
    public void shouldValidateTheRuleOfFieldsForFilmCreationNegative(
            String name,
            String description,
            LocalDate date,
            Integer duration,
            Class clazz,
            String exceptionMessage
    ) {
        Film film = new Film();
        film.setId(1);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(date);
        film.setDuration(duration);

        Validate filmValidator = new FilmValidationServiceImpl();
        assertThrows(clazz, () -> filmValidator.checkRules(film));
        try {
            filmValidator.checkRules(film);
        } catch (Exception e) {
            assertEquals(exceptionMessage, e.getMessage());
        }

    }

    private static Stream<Arguments> filmFields() {
        return Stream.of(
                Arguments.of(" ",
                        "Film description",
                        LocalDate.of(2023, 12, 3),
                        90,
                        ConditionNotMetException.class,
                        "Film name should be defined"
                ),
                Arguments.of(
                        "Name",
                        "a".repeat(201),
                        LocalDate.of(2023, 12, 3),
                        90,
                        ConditionNotMetException.class,
                        "Length of film description should be 200 chars or less"
                ),
                Arguments.of(
                        "Name",
                        "Film description",
                        LocalDate.of(1895, 12, 27),
                        90,
                        ConditionNotMetException.class,
                        "Film release should be after than 1895-12-28"
                ),
                Arguments.of(
                        "Name",
                        "Film description",
                        LocalDate.of(1985, 12, 29),
                        -90,
                        ConditionNotMetException.class,
                        "Film duration should be a positive number"
                )
        );
    }
}