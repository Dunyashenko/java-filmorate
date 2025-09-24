package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public List<User> getUserFriends(int id) {
        User user = userStorage.findOne(id);
        log.debug("Returning friends of user with id = {}", id);
        return user.getFriends().stream()
                .map(userStorage::findOne)
                .collect(Collectors.toList());
    }

    public List<Integer> addToFriends(int id, int friendId) {
        User user = userStorage.findOne(id);
        User otherUser = userStorage.findOne(friendId);
        log.debug("User with id {} adds user with id {} to friends", id, friendId);
        user.getFriends().add(friendId);
        otherUser.getFriends().add(id);
        return new ArrayList<>(user.getFriends());
    }

    public List<Integer> deleteFromFriends(int id, int friendId) {
        User user = userStorage.findOne(id);
        User otherUser = userStorage.findOne(friendId);
        log.debug("User with id {} deletes user with id {} from friends", id, friendId);
        user.getFriends().remove(friendId);
        otherUser.getFriends().remove(id);
        return new ArrayList<>(user.getFriends());
    }

    public List<User> getMutualFriends(int userId, int otherUserId) {
        Set<Integer> userFriends = userStorage.findOne(userId).getFriends();
        Set<Integer> otherUserFriends = userStorage.findOne(otherUserId).getFriends();
        log.info("Returning mutual friends of users with ids {} {}", userId, otherUserId);
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::findOne)
                .collect(Collectors.toList());
    }
}
