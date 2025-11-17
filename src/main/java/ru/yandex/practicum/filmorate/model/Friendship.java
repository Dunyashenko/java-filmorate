package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Friendship {
    private Integer userId;
    private Integer friendId;
    private Status status;
}
