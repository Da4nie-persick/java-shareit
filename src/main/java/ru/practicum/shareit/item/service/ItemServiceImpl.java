package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(ItemDto itemDto, Integer userId) {
        User user = userStorage.getUserId(userId);
        if (!userStorage.allUser().contains(user)) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        if (itemDto.getAvailable() == null || itemDto.getName().isEmpty() || itemDto.getDescription() == null) {
            throw new ValidationException("Статус, название или описание не должно быть пустым!");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);

        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(Integer itemId, ItemDto itemDto, Integer userId) {
        Item item = itemStorage.getItemId(itemId);
        if (item.getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        Item itemUpdate = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemStorage.update(itemId, itemUpdate));
    }

    @Override
    public ItemDto getItemId(Integer id) {
        return ItemMapper.toItemDto(itemStorage.getItemId(id));
    }

    @Override
    public List<ItemDto> searchItem(String query) {
        return itemStorage.searchItem(query).stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemByOwner(Integer userId) {
        return itemStorage.getItemByOwner(userId).stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }
}
