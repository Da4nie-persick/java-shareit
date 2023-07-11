package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

@Getter
@Setter
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
}
