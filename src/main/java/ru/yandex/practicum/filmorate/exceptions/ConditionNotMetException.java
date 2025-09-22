package ru.yandex.practicum.filmorate.exceptions;

public class ConditionNotMetException extends RuntimeException {

    public ConditionNotMetException(String message) {
        super(message);
    }
}
