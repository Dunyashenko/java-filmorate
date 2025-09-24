package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable int id) {
        return userStorage.findOne(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        return userStorage.create(newUser);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return userStorage.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<Integer> addToFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<Integer> deleteFromFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.deleteFromFriends(id, friendId);
    }
}
