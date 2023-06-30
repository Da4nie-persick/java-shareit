package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemInMemory implements ItemStorage {
    private final Map<Integer, Item> itemMap = new HashMap<>();
    private int id = 0;
    private final Map<Integer, List<Item>> itemByOwnerMap = new LinkedHashMap<>();

    @Override
    public Item create(Item item) {
        if (item.getOwner() == null) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        item.setId(++id);
        addOwnerList(item, item.getOwner());
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
        return itemByOwnerMap.get(userId);
    }

    private void addOwnerList(Item item, User user) {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        itemByOwnerMap.put(user.getId(), itemList);
    }
}
