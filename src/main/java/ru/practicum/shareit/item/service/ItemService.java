package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Integer userId);

    ItemDto update(Integer itemId, ItemDto itemDto, Integer userId);

    ItemDto getItemId(Integer id);

    List<ItemDto> searchItem(String query);

    List<ItemDto> getItemByOwner(Integer userId);
}
