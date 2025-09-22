package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.ConditionNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidationServiceImpl implements Validate {

    private static final Logger log = LoggerFactory.getLogger(UserValidationServiceImpl.class);

    @Override
    public void checkRules(Object o) {
        User user = (User) o;
        log.debug("Validating adherence to user field rules for user " + user.toString());
        checkEmailCondition(user.getEmail());
        checkLoginCondition(user.getLogin());
        checkNameCondition(user);
        checkBirthdayCondition(user.getBirthday());
    }

    public static void checkEmailCondition(String email) {
        if (email == null || email.isBlank()) {
            log.error("Email is empty");
            throw new ConditionNotMetException("Email should not be blank or empty");
        }

        if (!email.contains("@")) {
            log.error("Email " + email + " doesn't contain @ symbol");
            throw new ConditionNotMetException("Email should contain @");
        }
    }

    public static void checkLoginCondition(String login) {
        if (login == null || login.isBlank()) {
            log.error("Login is empty");
            throw new ConditionNotMetException("Login should not be blank or empty");
        }

        if (login.contains(" ")) {
            log.error("Login " + login + " has spaces");
            throw new ConditionNotMetException("Login should not contain any space");
        }
    }

    public static void checkNameCondition(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public static void checkBirthdayCondition(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            log.error("Birthday " + date + " is in the future");
            throw new ConditionNotMetException("Birthday cannot be in the future");
        }
    }
}
