package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exceptions.UnsupportedDataException;

public enum Rating {
    G("у фильма нет возрастных ограничений"),
    PG("детям рекомендуется смотреть фильм с родителями"),
    PG_13("детям до 13 лет просмотр не желателен"),
    R("лицам до 17 лет просматривать фильм можно только в присутствии взрослого"),
    NC_17("лицам до 18 лет просмотр запрещён");

    private final String description;

    Rating(String description) {
        this.description = description;
    }

    public static Rating fromString(String name) {
        if (name != null) {
            switch (name.toUpperCase()) {
                case "G" -> {
                    return G;
                }
                case "PG" -> {
                    return PG;
                }
                case "PG-13" -> {
                    return PG_13;
                }
                case "R" -> {
                    return R;
                }
                case "NC-17" -> {
                    return NC_17;
                }
                default -> throw new UnsupportedDataException("There is no such rating of Motion Picture Association");
            }
        } else {
            return G;
        }
    }
}
