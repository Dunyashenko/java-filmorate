package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film findOne(int id);

    Film create(Film film);

    Film update(Film newFilm);

    Integer delete(int id);
}
