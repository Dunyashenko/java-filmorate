package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.utils.ComparatorByFilmPopularity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    public List<Integer> addLikeToFilm(int filmId, int userId) {
        User user = userStorage.findOne(userId);
        log.debug("Adding like to film with id {} of user with id {}", filmId, userId);
        Set<Integer> filmLikes = filmStorage.findOne(filmId).getLikes();
        filmLikes.add(user.getId());
        return new ArrayList<>(filmLikes);
    }

    public List<Integer> deleteLikeFromFilm(int filmId, int userId) {
        User user = userStorage.findOne(userId);
        log.debug("Deleting like from film with id {} of user with id {}", filmId, userId);
        Set<Integer> filmLikes = filmStorage.findOne(filmId).getLikes();
        filmLikes.remove(user.getId());
        return new ArrayList<>(filmLikes);
    }

    public List<Film> getTopPopularFilms(int topCount) {
        log.info("Returning a list of most popular films by likes");
        return filmStorage.findAll().stream()
                .sorted(new ComparatorByFilmPopularity())
                .limit(topCount == 0 ? 10 : topCount)
                .collect(Collectors.toList());
    }
}
