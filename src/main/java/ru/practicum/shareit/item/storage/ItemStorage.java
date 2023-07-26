package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    Item update(Integer itemId, Item item);

    Item getItemId(Integer id);

    List<Item> searchItem(String query);

    List<Item> getItemByOwner(Integer userId);
}
