package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemInMemory implements ItemStorage {
    private final Map<Integer, Item> itemMap = new HashMap<>();
    private int id = 0;

    @Override
    public Item create(Item item) {
        item.setId(++id);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Integer itemId, Item item) {
        Item updateItem = itemMap.get(itemId);

        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        itemMap.put(itemId, updateItem);
        return updateItem;
    }

    @Override
    public Item getItemId(Integer id) {
        return itemMap.get(id);
    }

    @Override
    public List<Item> searchItem(String query) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        }

        return itemMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(query.toLowerCase()))
                        || (item.getDescription().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemByOwner(Integer userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }
}
