package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidationServiceImpl;
import ru.yandex.practicum.filmorate.service.Validate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private final Validate userValidator = new UserValidationServiceImpl();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        log.info("Starting creation of user = {}", newUser);
        userValidator.checkRules(newUser);

        if (users.values().stream().anyMatch(user -> user.getEmail().equals(newUser.getEmail()))) {
            log.error("User with email = {} already exists", newUser.getEmail());
            throw new DuplicatedDataException("User with email = " + newUser.getEmail() + " already exists");
        }

        newUser.setId(getNextId());
        log.info("User with id {} has successfully been created", newUser.getId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("User without id cannot be updated");
            throw new ConditionNotMetException("Id should be defined");
        }

        if (users.containsKey(newUser.getId())) {
            log.info("Starting updating user with id {}", newUser.getId());
            User user = users.get(newUser.getId());

            if (newUser.getEmail() != null) {
                UserValidationServiceImpl.checkEmailCondition(newUser.getEmail());
                log.debug("Updating old field email from {} to a new {}", user.getEmail(), newUser.getEmail());
                user.setEmail(newUser.getEmail());
            }

            if (newUser.getLogin() != null) {
                UserValidationServiceImpl.checkLoginCondition(newUser.getLogin());
                log.debug("Updating old field login from {} to a new {}", user.getLogin(), newUser.getLogin());
                user.setLogin(newUser.getLogin());
            }

            if (newUser.getName() != null) {
                log.debug("Updating old field name from {} to a new {}", user.getName(), newUser.getName());
                user.setName(newUser.getName());
            }

            if (newUser.getBirthday() != null) {
                UserValidationServiceImpl.checkBirthdayCondition(newUser.getBirthday());
                log.debug("Updating old field birthday from {} to a new {}", user.getBirthday(), newUser.getBirthday());
                user.setBirthday(newUser.getBirthday());
            }
            log.info("User with id {} has successfully been updated", newUser.getId());
            return user;
        } else {
            log.error("User with id = " + newUser.getId() + " cannot be found");
            throw new NotFoundException("User with id = " + newUser.getId() + " cannot be found");
        }
    }

    private int getNextId() {
        int currentId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }
}
