package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Integer userId);

    //ItemDto create(ItemDto itemDto, Integer userId, Integer requestId);

    ItemDto update(Integer itemId, ItemDto itemDto, Integer userId);

    ItemWithBookingDto getItemId(Integer userId, Integer id);

    List<ItemDto> searchItem(String query, Integer size, Integer from);

    List<ItemWithBookingDto> getItemByOwner(Integer userId, Integer size, Integer from);

    CommentDto addComment(CommentDto commentDto, Integer userId, Integer itemId);
}
