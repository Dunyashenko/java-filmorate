package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ConditionNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserValidationServiceImplTest {

    @Test
    public void shouldValidateTheRuleOfFieldsForUSerCreationPositive() {
        User user = new User();
        user.setId(1);
        user.setEmail("example@email.com");
        user.setLogin("username");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 10, 12));

        Validate userValidator = new UserValidationServiceImpl();
        assertDoesNotThrow(() -> userValidator.checkRules(user));
    }

    @ParameterizedTest
    @MethodSource("userFields")
    public void shouldValidateTheRuleOfFieldsForUserCreationNegative(
            String email,
            String login,
            String name,
            LocalDate birthday,
            Class clazz,
            String exceptionMessage
    ) {
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);

        Validate userValidator = new UserValidationServiceImpl();
        assertThrows(clazz, () -> userValidator.checkRules(user));
        try {
            userValidator.checkRules(user);
        } catch (Exception e) {
            assertEquals(exceptionMessage, e.getMessage());
        }

    }

    private static Stream<Arguments> userFields() {
        return Stream.of(
                Arguments.of(" ",
                        "username",
                        "Name",
                        LocalDate.of(2000, 12, 12),
                        ConditionNotMetException.class,
                        "Email should not be blank or empty"
                ),
                Arguments.of("exampleemail.com",
                        "username",
                        "Name",
                        LocalDate.of(2000, 12, 12),
                        ConditionNotMetException.class,
                        "Email should contain @"
                ),
                Arguments.of("example@email.com",
                        " ",
                        "Name",
                        LocalDate.of(2000, 12, 12),
                        ConditionNotMetException.class,
                        "Login should not be blank or empty"
                ),
                Arguments.of("example@email.com",
                        "username ",
                        "Name",
                        LocalDate.of(2000, 12, 12),
                        ConditionNotMetException.class,
                        "Login should not contain any space"
                ),
                Arguments.of("example@email.com",
                        "username",
                        "Name",
                        LocalDate.of(2025, 12, 12),
                        ConditionNotMetException.class,
                        "Birthday cannot be in the future"
                )
        );
    }

}